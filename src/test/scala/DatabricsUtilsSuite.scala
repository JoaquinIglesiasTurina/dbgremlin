import DatabricksUtils._
import com.databricks.sdk.service.jobs.Job
import org.scalamock.stubs.Stubs

import scala.util.Success

class GetDependentJobIdsSuite extends munit.FunSuite:
  test("get dependent job ids"):
    val dependentJobs = getDependentJobIds(inputJob)
    assertEquals(dependentJobs, Success(List(1234L)))
  test("get dependent job ids with empty job"):
    val inputJob = new Job()
    assert(getDependentJobIds(inputJob).isFailure)

class GetAllDependentJobsSuite extends munit.FunSuite, Stubs:
  test("No dependencies"):
    val mockWorkspaceClient = MockWorkspaceClient()
    val output = getAllDependentJobs(mockWorkspaceClient, inputJob)
    assert(output.head == inputJob)
