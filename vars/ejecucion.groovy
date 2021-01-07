def call(){

    pipeline {
        agent any

        stages {
            stage('pipeline') {
                steps {
                    script {
                        
                        if(env.GIT_BRANCH=='develop' || env.GIT_BRANCH.contains('feature'))
                        {
                            gradleCI.call();
                        } else if(env.GIT_BRANCH.contains('release')) {
                            gradleCD.call();
                        } else {
                            // Do Nothing.
                        }
                    }
                }
            }
        }
    }

}

return this;