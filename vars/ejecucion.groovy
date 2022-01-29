
def call(){
 
   
 pipeline {

        agent any

        environment{
         STAGE=''
         STAGE_BUILD='build'
         STAGE_TEST='test'
         STAGE_SONAR='sonar'
         STAGE_NEXUS='nexus'
         STAGE_RUN='run'
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
                     println 'Pipeline'
                     println 'stage:'+params.stage
                     List<String> paramAllowed=getStageForExecution(params.stage)
                     paramAllowed.each {
                        println 'elemento:'+it
                     }
                     
                    
                     println params.buildTool

                     if(params.buildTool=='gradle'){
                                           
                          gradle.call(paramAllowed)

                     }
                     else{
                      println 'Ejecutar maven'
                      maven.call(paramAllowed)
                       

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
def getStageForExecution(String params){
  return params.split(';').toList();
 
}

return this;
