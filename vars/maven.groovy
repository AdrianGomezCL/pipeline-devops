def call(){
    
    if(util.validateStage('compile'))
    {
        stage('compile') {
            bat './mvnw.cmd clean compile -e'
        }
    }

    if(util.validateStage('test'))
    {
        stage('test'){
            bat './mvnw.cmd clean test -e'
        }
    }

    if(util.validateStage('jar'))
    {
        stage('jar'){
            bat './mvnw.cmd clean package -e'
        }
    }

    if(util.validateStage('sonar'))
    {
        stage('sonar') {
            // Nombre extraido desde Jenkins > Global tool configuration > SonarQube Scanner
            def scannerHome = tool 'sonar-scanner';

            // Nombre extraido desde Jenkins > Configurar el sistema > SonarQube servers
            withSonarQubeEnv('sonar-server') {
                bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
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