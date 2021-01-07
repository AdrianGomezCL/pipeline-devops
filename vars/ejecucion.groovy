def call(){

    pipeline {
        agent any

        stages {
            stage('pipeline') {
                steps {
                    script {

                        switch(env.GIT_BRANCH)
                        {
                            case "develop": case "feature":
                                gradleCI.call();
                            break;
                            case "release":
                                gradleCD.call();
                            break;
                        }
                    }
                }
            }
        }
    }

}

return this;