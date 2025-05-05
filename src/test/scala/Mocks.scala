import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.core.ApiClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.JobSettings
import com.databricks.sdk.service.jobs.JobsAPI
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.Task

import scala.jdk.CollectionConverters._

val runJobTask = RunJobTask().setJobId(1234L)
val task = Task().setRunJobTask(runJobTask)
val settings = JobSettings().setTasks(Seq(task).asJava)
val inputJob = Job().setSettings(settings).setJobId(4321L)

class MockJobsAPI(apiClient: ApiClient) extends JobsAPI(apiClient: ApiClient):
  override def get(jobId: Long) = 
      val settings = JobSettings().setTasks(Seq().asJava)
      Job().setJobId(jobId).setSettings(settings)
class  MockWorkspaceClient() extends WorkspaceClient:
  override def jobs(): MockJobsAPI = MockJobsAPI(ApiClient())
