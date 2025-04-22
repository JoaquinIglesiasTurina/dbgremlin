import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.Task
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.JobSettings

import scala.jdk.CollectionConverters._
import scala.util.Success

class DatabricsUtilsSuite extends munit.FunSuite {
  test("get dependent job ids") {
    val runJobTask = RunJobTask().setJobId(1234L)
    val task = Task().setRunJobTask(runJobTask)
    val settings = JobSettings().setTasks(Seq(task).asJava)
    val inputJob = Job().setSettings(settings).setJobId(4321L)

    val dependentJobs = getDependentJobIds(inputJob)
    assertEquals(dependentJobs, Success(List(1234L)))
  }

  test("get dependent job ids with empty job") {
    val inputJob = new Job()
    assert(getDependentJobIds(inputJob).isFailure)
  }
}