def call(){

    pipeline {
        agent any

        parameters {
            choice(
                name:'compileTool',
                choices: ['Gradle', 'Maven'],
                description: 'Seleccione herramienta de compilacion'
            )
            string(
                name: 'stage',
                defaultValue: '',
                description: 'Seleccione stage a ejecutar (Dejar en blanco para ejecutar todos)'
            )
        }

        stages {
            stage('pipeline') {
                steps {
                    script {
                        
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
    }

}

return this;