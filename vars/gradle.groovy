import org.cl.Util

def call(){

    // Variable para definir si los steps anteriores han sido correctos
    // Comienza en false permitiendo que se valide inicie de step en build & test
    def success = false

    // Importacion de funciones desde Util
    def util = new Util()

    // Despliegue de sistema operativo desde donde se corre pipeline (Para definir sh o bat)
    def so = isUnix() ? 'Linux' : 'Windows'
    figlet so

    figlet 'Gradle'

    if(util.validateStage('build') || util.validateStage('test'))
    {
        stage('build & test') {

            try {
                if (isUnix()) {
                    sh "gradle clean build"
                } else {
                    bat "gradle clean build"
                }

                sucess = true

            } catch(Exception e) {
                echo "Error en stage build: "+e
            }
            

        }
    }

    if(util.validateStage('sonar') && success)
    {
        stage('sonar') {

            try {
                // Nombre extraido desde Jenkins > Global tool configuration > SonarQube Scanner
                def scannerHome = tool 'sonar-scanner';

                // Nombre extraido desde Jenkins > Configurar el sistema > SonarQube servers
                withSonarQubeEnv('sonar-server') {

                    if (isUnix()) {
                        sh "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
                    } else {
                        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
                    }
                }

                success = true

            } catch(Exception e) {
                echo "Error en stage sonar: "+e
            }
        }
    }

    if(util.validateStage('run') && success)
    {
        stage('run') {

            try {
                if (isUnix()) {
                    sh "nohup bash gradlew bootRun &"
                } else {
                    bat 'start /B gradle bootRun'
                }

                success = true

                sleep 20

            } catch(Exception e) {
                echo "Error en stage run: "+e
            }
        }
    }

    if(util.validateStage('rest') && success)
    {
        stage('rest') {

            try {
                if (isUnix()) {
                    sh 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
                } else {
                    bat 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
                }

                success = true
            } catch(Exception e) {
                echo "Error en stage rest: "+e
            }

        }
    }

    if(util.validateStage('nexus') && success)
    {
        stage('nexus') {

            try {
                nexusPublisher nexusInstanceId: 'NexusLocal',
                    nexusRepositoryId: 'test-nexus',
                    packages: [
                        [
                            $class: 'MavenPackage',
                            mavenAssetList: [
                                [
                                    classifier: '',
                                    extension: 'jar',
                                    filePath: 'C:\\proyects\\diplomado\\gradle\\ejemplo-gradle\\build\\DevOpsUsach2020-0.0.1.jar'
                                ]
                            ],
                            mavenCoordinate: [
                                artifactId: 'DevOpsUsach2020',
                                groupId: 'com.devopsusach2020',
                                packaging: 'jar',
                                version: '0.0.1'
                            ]
                        ]
                    ]
            } catch (Exception e) {
                echo "Error en stage nexus: "+e
            }
        }
    }
}

return this;