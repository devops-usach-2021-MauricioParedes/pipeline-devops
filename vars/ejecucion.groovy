
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
 def stages=[STAGE_BUILD,STAGE_SONAR,STAGE_RUN,STAGE_TEST,STAGE_NEXUS]
 if(params=='')
 {
    return stages
 }
 else
 {
  def stagesSelected=params.split(';').toList()
  stagesSelected.each{ val-> 
    if(stages.any{it==val}==false)
    {
       error "stage not found. You can select ${STAGE_BUILD};${STAGE_TEST};${STAGE_RUN};${STAGE_SONAR};${STAGE_NEXUS}"
       println "stage not found"
      return
    }
  }
  
  if(stagesSelected.any{it==STAGE_NEXUS} && !stagesSelected.any{it==STAGE_BUILD})
  {
    error "If you select ${STAGE_NEXUS} you need use ${STAGE_BUILD} too"
    println "Ejecución fallida por validacion"
    return
   
  }
  if(stagesSelected.any{it==STAGE_RUN} && !stagesSelected.any{it==STAGE_BUILD})
  {
    error "If you select ${STAGE_RUN} you need use ${STAGE_BUILD} too"
    println "Ejecución fallida por validacion"
    return
   
  }
  if(stagesSelected.any{it==STAGE_TEST} && !stagesSelected.any{it==STAGE_RUN})
  {
    error "If you select ${STAGE_TEST} you need use ${STAGE_RUN} too"
    println "Ejecución fallida por validacion"
    return
   
  }
  return stagesSelected
 }
  
 
}

return this;
