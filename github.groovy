import com.atlassian.jira.component.ComponentAccessor
import com.opensymphony.workflow.InvalidInputException

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def repoName = customFieldManager.getCustomFieldObjectByName("Git repositories")
def repoNameValue = issue.getCustomFieldValue(repoName)?.value
//GitHub_Api_PAT_Token
  def GH_API_TOKEN="xxxx"
  //Pass the Repo Name
  def post = new URL("https://api.github.com/user/repos?access_token=${GH_API_TOKEN}").openConnection();
  def message = "{\"name\": \"${repoNameValue}\", \"description\": \"This is your first repository\"}"
  post.setRequestMethod("POST")
  post.setDoOutput(true)
  post.setRequestProperty("Content-Type", "application/json")
  post.getOutputStream().write(message.getBytes("UTF-8"));
  def postRC = post.getResponseCode();
  println(postRC);
  if(postRC.equals(201)) {
      println(post.getInputStream().getText());      
  }
else 
    invalidInputException = new InvalidInputException("Git Pipeline Creation failed.")
