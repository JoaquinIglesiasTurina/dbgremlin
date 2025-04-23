import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.Task
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.JobSettings

import org.scalamock.stubs.Stubs
import scala.jdk.CollectionConverters._
import scala.util.Success
import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.JobsAPI
import com.databricks.sdk.core.ApiClient

val runJobTask = RunJobTask().setJobId(1234L)
val task = Task().setRunJobTask(runJobTask)
val settings = JobSettings().setTasks(Seq(task).asJava)
val inputJob = Job().setSettings(settings).setJobId(4321L)

class GetDependentJobIdsSuite extends munit.FunSuite:
  test("get dependent job ids"):
    val dependentJobs = getDependentJobIds(inputJob)
    assertEquals(dependentJobs, Success(List(1234L)))
  

  test("get dependent job ids with empty job"):
    val inputJob = new Job()
    assert(getDependentJobIds(inputJob).isFailure)

class MockJobsAPI(apiClient: ApiClient) extends JobsAPI(apiClient: ApiClient):
  override def get(jobId: Long) = 
      val settings = JobSettings().setTasks(Seq().asJava)
      Job().setJobId(jobId).setSettings(settings)
  
class  MockWorkspaceClient() extends WorkspaceClient:
  override def jobs(): MockJobsAPI = MockJobsAPI(ApiClient())

class GetAllDependentJobsSuite extends munit.FunSuite, Stubs:
  test("No dependencies"):
    val mockWorkspaceClient = MockWorkspaceClient()
    val output = getAllDependentJobs(mockWorkspaceClient, inputJob)
    assert(output.head == inputJob)
