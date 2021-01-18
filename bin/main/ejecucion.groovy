def call(){

    pipeline {
        agent any

        parameters {
            choice (
                name:'compileTool',
                choices: ['Gradle', 'Maven'],
                description: 'Seleccione herramienta de compilacion'
            )
            string (
                name: 'stage',
                defaultValue: '',
                description: 'Seleccione stage a ejecutar (Dejar en blanco para ejecutar todos)'
            )
            string (
                name: 'releaseVersion',
                defaultValue: '',
                description: 'Ingresar version formato: {major}-{minor}-{patch}, ejemplo: 0-0-0'
            )
        }

        stages {
            stage('pipeline') {
                steps {
                    script {
                        
                        env.RELEASE_VERSION = params.releaseVersion
                        
                        switch(params.compileTool)
                        {
                            case 'Gradle':
                                gradle.call()
                            break;
                            case 'Maven':
                                maven.call()
                            break;
                        }
                    }
                }
            }
        }

        post {
            success {
                slackSend message: '[Grupo 4] [Pipeline IC] [Rama: '+env.GIT_BRANCH+'] [Stage: ' + env.JOB_NAME + '] [' + env.compileTool  + '] [Resultado: Ok]'
            }

            failure {
                slackSend message: '[Grupo 4] [Pipeline IC] [Rama: '+env.GIT_BRANCH+'] [Stage: ' + env.JOB_NAME + '] [' + env.compileTool  + '] [Resultado: No Ok]'
            }
        }
    }

}

return this;