import com.atlassian.jira.component.ComponentAccessor
import com.opensymphony.workflow.InvalidInputException


def customFieldManager = ComponentAccessor.getCustomFieldManager()
def repoName = customFieldManager.getCustomFieldObjectByName("Git repositories")
def pipelinename = customFieldManager.getCustomFieldObjectByName("CI BuildJob")
def techstackname = customFieldManager.getCustomFieldObjectByName("Tech Stack")
def repoNameValue = issue.getCustomFieldValue(repoName)?.value
def piplelineNamevalue = issue.getCustomFieldValue(pipelinename)?.value
def techstackNameValue = issue.getCustomFieldValue(techstackname)?.value
def BUILD_TOKEN = "11268982e8e7f9d983d79b37bbeb5e4d1d"
def urlConnection = new URL("http://3.14.131.151:8080/job/DSL_Demo_Seed/buildWithParameters?token=${BUILD_TOKEN}&Gitrepositories=${repoNameValue}&techstack=${techstackNameValue}&name_job_create=${piplelineNamevalue}").openConnection()
def message = "{\"name\": \"${piplelineNamevalue}\", \"description\": \"This is your first pipleline\"}"
def authString = "admin:11268982e8e7f9d983d79b37bbeb5e4d1d".getBytes().encodeBase64().toString()
def out = new StringBuffer()
def err = new StringBuffer()

def command = "curl -XGET \"http://3.14.131.151:8080/checkJobName?value=${piplelineNamevalue}\" --user admin:${BUILD_TOKEN}"
Process process = command.execute()
process.consumeProcessOutput( out, err )
process.waitFor()

if( out.size() == 7 ) {
    urlConnection.setDoOutput(true)
    urlConnection.setRequestMethod("POST")
    urlConnection.setRequestProperty("Authorization", "Basic ${authString}")
    urlConnection.setRequestProperty("Content-Type", "application/json")
    def postRC = urlConnection.getResponseCode();

    println(urlConnection.getInputStream().getText())
    if(postRC.equals(201)) {
        println(post.getInputStream().getText());      
    }
    else 
        invalidInputException = new InvalidInputException("Jenkins Pipeline Creation failed.")
}else{
    invalidInputException = new InvalidInputException("Jenkins Pipeline Creation failed.")
}

log.warn "=== Script Ended ==="
