def call(){
 
   
 pipeline {

        agent any

        environment{
         STAGE=''
         
       }

        parameters {
                choice (choices: ['gradle', 'maven'], description: 'Indicar herramienta de contrucción', name: 'buildTool')
                string(name:'stage',defaultValue:'',description:'Write stages that you need execute or keep blank to execute all (example: build)')
        }

        stages{
                stage('Pipeline'){
                        steps{
                         script{

                    try{
                     figlet 'Pipeline'
                                                        
                     figlet params.buildTool
                                          
                     if(params.buildTool=='gradle'){
                          gradle.call()

                     }
                     else{
                      
                          maven.call()
                       

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
