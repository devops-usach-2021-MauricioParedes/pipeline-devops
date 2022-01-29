
def call(String paramStage){
 println paramStage
 pipeline {

        agent any

        environment{
        STAGE=''
    }

        parameters {
                choice choices: ['gradle', 'maven'], description: 'Indicar herramienta de contrucción', name: 'buildTool'
        }

        stages{
                stage('Pipeline'){
                        steps{
                                script{

                    try{
                    println 'Pipeline'
                    println '2-'+ paramStage
                                        println params.buildTool

                                        if(params.buildTool=='gradle'){
                                            println 'Ejecutar gradle'
                                                gradle()

                                        }
                                        else{
                                            println 'Ejecutar maven'
                                            maven()
                       

                                        }
                                        slackSend color: 'good', message: "[${env.USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución exitosa"

                    }
                    catch(Exception e){
                        slackSend color: 'danger', message: "[${env.USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage [${STAGE}]"
                                                error "Ejecución fallida en stage ${STAGE}"
                    }




                                }
                        }

                }

    }
} 
 
    

}

return this;
