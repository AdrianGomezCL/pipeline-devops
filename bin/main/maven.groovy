import org.cl.*

def call(){
    
    def util = new Util()

    def so = isUnix() ? 'Linux' : 'Windows'

    figlet so
    figlet 'Maven'
    
    if(validateStage('compile'))
    {
        stage('compile') {

            if (isUnix()) {
                sh './mvnw.cmd clean compile -e'
            } else {
                bat './mvnw.cmd clean compile -e'
            }
        }
    }

    if(validateStage('test'))
    {
        stage('test'){

            if (isUnix()) {
                sh './mvnw.cmd clean test -e'
            } else {
                bat './mvnw.cmd clean test -e'
            }

            
        }
    }

    if(validateStage('jar'))
    {
        stage('jar'){

            if (isUnix()) {
                sh './mvnw.cmd clean package -e'
            } else {
                bat './mvnw.cmd clean package -e'
            }
        }
    }

    if(validateStage('sonar'))
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

    if(validateStage('nexus'))
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