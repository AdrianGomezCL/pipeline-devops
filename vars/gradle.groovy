import org.cl.Util

def call(){

    def util = new Util()

    isUnix() ? figlet 'Linux' : figlet 'Windows'

    figlet 'Gradle'

    if(util.validateStage('build') || util.validateStage('test'))
    {
        stage('build & test') {

            if (isUnix()) {
                sh "gradle clean build"
            } else {
                bat "gradle clean build"
            }
        }
    }

    if(util.validateStage('sonar'))
    {
        stage('sonar') {
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
        }
    }

    if(util.validateStage('run'))
    {
        stage('run') {

            if (isUnix()) {
                sh "nohup bash gradlew bootRun &"
            } else {
                bat 'start /B gradle bootRun'
            }

            sleep 20
        }
    }

    if(util.validateStage('rest'))
    {
        stage('rest') {

            if (isUnix()) {
                sh 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
            } else {
                bat 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
            }
        }
    }

    if(util.validateStage('nexus'))
    {
        stage('nexus') {
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
        }
    }
}

return this;