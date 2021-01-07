def call() {
    figlet 'Continuous Delivery'

    stage("downloadNexus"){    
        env.TAREA =  env.STAGE_NAME       
        bat 'curl -X GET -u admin:admin http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O' 
        downloadOK = true;
    }

    stage('runDownloadedJar') {
        //bat 'java -jar DevOpsUsach2020-0.0.1.jar'
        bat 'start /B gradle bootRun'
        sleep 20
    }

    stage('rest') {
        bat 'curl -X GET "http://localhost:8082/rest/mscovid/test?msg=testing"'
    }

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
                            filePath: 'DevOpsUsach2020-0.0.1.jar'
                        ]
                    ],
                    mavenCoordinate: [
                        artifactId: 'DevOpsUsach2020',
                        groupId: 'com.devopsusach2020',
                        packaging: 'jar',
                        version: 'release-v1.0.0'
                    ]
                ]
            ]
    }  
}