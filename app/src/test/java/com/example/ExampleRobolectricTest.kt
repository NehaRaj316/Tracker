package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FocusCGL 31", appName)
  }

  @Test
  fun `verify daily schedule template`() {
    val slots = com.example.data.model.ScheduleTemplate.dailySlots
    assertEquals(13, slots.size)
    // 10:00 AM start, 10:00 PM end
    assertEquals(600, slots.first().startMinuteOfDay)
    assertEquals(1320, slots.last().endMinuteOfDay)
  }

  @Test
  fun `verify slot color assignments`() {
    val studyColor = com.example.ui.components.getSlotColor(com.example.data.model.SlotType.STUDY_QUANT)
    assertEquals(com.example.ui.components.ScheduleColors.Study, studyColor)

    val mockColor = com.example.ui.components.getSlotColor(com.example.data.model.SlotType.MOCK_TEST)
    assertEquals(com.example.ui.components.ScheduleColors.MockTest, mockColor)

    val lunchColor = com.example.ui.components.getSlotColor(com.example.data.model.SlotType.LUNCH)
    assertEquals(com.example.ui.components.ScheduleColors.Meal, lunchColor)

    val walkColor = com.example.ui.components.getSlotColor(com.example.data.model.SlotType.WALKING)
    assertEquals(com.example.ui.components.ScheduleColors.Walk, walkColor)
  }

  @Test
  fun `verify DayPlan progress and completion tracking`() {
    val plan = com.example.data.model.DayPlan(
      dayNumber = 1,
      phase = 1,
      title = "Foundation Day 1",
      quantTopic = "Number Systems",
      reasoningTopic = "Analogy",
      englishTopic = "Error Spotting",
      gaTopic = "Ancient History",
      mockTestName = "Mini Mock 1",
      quantCompleted = true,
      reasoningCompleted = true,
      mockCompleted = true
    )
    assertEquals(10, plan.totalTaskCount())
    assertEquals(3, plan.completedTaskCount())
    assertEquals(0.3f, plan.progressPercent(), 0.001f)
  }

  @Test
  fun `verify MockTest score calculations`() {
    val mock = com.example.data.model.MockTest(
      testNumber = 1,
      title = "SSC CGL Tier-1 Mock #1",
      provider = "Testbook",
      isAttempted = true,
      quantScore = 42.5f,
      reasoningScore = 45.0f,
      englishScore = 38.0f,
      gaScore = 26.5f,
      totalScore = 152.0f,
      attemptedQuestions = 92,
      correctAnswers = 80,
      wrongAnswers = 12,
      accuracyPercent = (80f / 92f) * 100f
    )
    assertEquals(152.0f, mock.totalScore, 0.001f)
    org.junit.Assert.assertTrue(mock.accuracyPercent > 86f)
    org.junit.Assert.assertTrue(mock.isAttempted)
  }

  @Test
  fun `verify 30-day curriculum block and phase calculations`() {
    val initialPlans = com.example.data.db.InitialData.get30DayPlans()
    assertEquals(30, initialPlans.size)

    val totalGoalBlocks = initialPlans.size * 10
    assertEquals(300, totalGoalBlocks)

    val phase1Blocks = initialPlans.filter { it.phase == 1 }.size * 10
    val phase2Blocks = initialPlans.filter { it.phase == 2 }.size * 10
    val phase3Blocks = initialPlans.filter { it.phase == 3 }.size * 10

    assertEquals(100, phase1Blocks) // Days 1 to 10 (Foundation)
    assertEquals(100, phase2Blocks) // Days 11 to 20 (Speed & Depth)
    assertEquals(100, phase3Blocks) // Days 21 to 30 (Simulation & Revision)
  }

  @Test
  fun `verify PomodoroMode durations and properties`() {
    val pomo25 = com.example.data.model.PomodoroMode.POMODORO_25
    assertEquals(25, pomo25.durationMinutes)
    org.junit.Assert.assertFalse(pomo25.isBreak)

    val deep50 = com.example.data.model.PomodoroMode.DEEP_FOCUS_50
    assertEquals(50, deep50.durationMinutes)
    org.junit.Assert.assertFalse(deep50.isBreak)

    val shortBreak = com.example.data.model.PomodoroMode.SHORT_BREAK
    assertEquals(5, shortBreak.durationMinutes)
    org.junit.Assert.assertTrue(shortBreak.isBreak)

    val longBreak = com.example.data.model.PomodoroMode.LONG_BREAK
    assertEquals(15, longBreak.durationMinutes)
    org.junit.Assert.assertTrue(longBreak.isBreak)

    val fullBlock = com.example.data.model.PomodoroMode.FULL_SESSION
    assertEquals(0, fullBlock.durationMinutes) // Uses slot duration
    org.junit.Assert.assertFalse(fullBlock.isBreak)
  }

  @Test
  fun `verify MockTest score progression and trajectory calculations`() {
    val mock1 = com.example.data.model.MockTest(
      testNumber = 1,
      title = "SSC CGL Full Mock #1",
      provider = "Testbook",
      isAttempted = true,
      quantScore = 32.5f,
      reasoningScore = 38.0f,
      englishScore = 30.0f,
      gaScore = 18.0f,
      totalScore = 118.5f,
      attemptedQuestions = 80,
      correctAnswers = 65,
      wrongAnswers = 15,
      accuracyPercent = 81.25f
    )

    val mock2 = com.example.data.model.MockTest(
      testNumber = 2,
      title = "SSC CGL Full Mock #2",
      provider = "Oliveboard",
      isAttempted = true,
      quantScore = 38.0f,
      reasoningScore = 42.5f,
      englishScore = 35.0f,
      gaScore = 22.0f,
      totalScore = 137.5f,
      attemptedQuestions = 88,
      correctAnswers = 74,
      wrongAnswers = 14,
      accuracyPercent = 84.09f
    )

    val mock3 = com.example.data.model.MockTest(
      testNumber = 3,
      title = "SSC CGL Full Mock #3",
      provider = "PYQ 2024 Shift",
      isAttempted = true,
      quantScore = 44.0f,
      reasoningScore = 46.5f,
      englishScore = 39.0f,
      gaScore = 25.5f,
      totalScore = 155.0f,
      attemptedQuestions = 94,
      correctAnswers = 82,
      wrongAnswers = 12,
      accuracyPercent = 87.23f
    )

    val attemptedList = listOf(mock1, mock2, mock3)
    val latest = attemptedList.last()
    val first = attemptedList.first()
    val totalGain = latest.totalScore - first.totalScore
    val avgScore = attemptedList.map { it.totalScore }.average().toFloat()

    // Test trajectory and delta
    assertEquals(36.5f, totalGain, 0.001f) // 155.0 - 118.5 = +36.5
    assertEquals(137.0f, avgScore, 0.001f) // (118.5 + 137.5 + 155.0) / 3 = 137.0
    org.junit.Assert.assertTrue(latest.totalScore >= 140f) // Crosses SSC CGL Tier-1 cutoff

    // Verify delta from previous mock
    val prevMock = attemptedList[attemptedList.lastIndex - 1]
    val lastDelta = latest.totalScore - prevMock.totalScore
    assertEquals(17.5f, lastDelta, 0.001f) // 155.0 - 137.5 = +17.5
  }

  @Test
  fun `verify CglSubject sectional performance drill-down calculations`() {
    val mock1 = com.example.data.model.MockTest(
      testNumber = 1,
      title = "Mock 1",
      provider = "Testbook",
      isAttempted = true,
      quantScore = 36.0f,
      reasoningScore = 44.0f,
      englishScore = 38.0f,
      gaScore = 20.0f,
      totalScore = 138.0f
    )
    val mock2 = com.example.data.model.MockTest(
      testNumber = 2,
      title = "Mock 2",
      provider = "Oliveboard",
      isAttempted = true,
      quantScore = 42.0f,
      reasoningScore = 46.0f,
      englishScore = 40.0f,
      gaScore = 24.0f,
      totalScore = 152.0f
    )
    val mocks = listOf(mock1, mock2)

    val mathsScores = com.example.ui.components.CglSubject.MATHS.getScoresFromMocks(mocks)
    assertEquals(2, mathsScores.size)
    assertEquals(36.0f, mathsScores[0], 0.001f)
    assertEquals(42.0f, mathsScores[1], 0.001f)
    assertEquals(39.0f, mathsScores.average().toFloat(), 0.001f)
    assertEquals(42.0f, mathsScores.maxOrNull() ?: 0f, 0.001f)

    val reasoningScores = com.example.ui.components.CglSubject.REASONING.getScoresFromMocks(mocks)
    assertEquals(45.0f, reasoningScores.average().toFloat(), 0.001f)
    org.junit.Assert.assertTrue(reasoningScores.average() >= 40.0) // Strong cutoff safe

    val englishScores = com.example.ui.components.CglSubject.ENGLISH.getScoresFromMocks(mocks)
    assertEquals(39.0f, englishScores.average().toFloat(), 0.001f)

    val gsScores = com.example.ui.components.CglSubject.GS.getScoresFromMocks(mocks)
    assertEquals(22.0f, gsScores.average().toFloat(), 0.001f)

    // Test DayPlan topic retrieval per subject
    val samplePlan = com.example.data.db.InitialData.get30DayPlans().first()
    val mathsTopic = com.example.ui.components.CglSubject.MATHS.getTopicForDay(samplePlan)
    val englishTopic = com.example.ui.components.CglSubject.ENGLISH.getTopicForDay(samplePlan)
    val reasoningTopic = com.example.ui.components.CglSubject.REASONING.getTopicForDay(samplePlan)
    val gsTopic = com.example.ui.components.CglSubject.GS.getTopicForDay(samplePlan)

    org.junit.Assert.assertTrue(mathsTopic.isNotBlank())
    org.junit.Assert.assertTrue(englishTopic.isNotBlank())
    org.junit.Assert.assertTrue(reasoningTopic.isNotBlank())
    org.junit.Assert.assertTrue(gsTopic.isNotBlank())
  }

  @Test
  fun `verify 31-day roadmap default topic revisions generation`() {
    val revisions = com.example.data.db.InitialData.getDefaultTopicRevisions()
    // 31 days * 4 subjects = 124 topics
    assertEquals(124, revisions.size)

    val quantTopics = revisions.filter { it.subject == "Quantitative Aptitude" }
    val reasoningTopics = revisions.filter { it.subject == "Reasoning" }
    val englishTopics = revisions.filter { it.subject == "English" }
    val gaTopics = revisions.filter { it.subject == "General Awareness" }

    assertEquals(31, quantTopics.size)
    assertEquals(31, reasoningTopics.size)
    assertEquals(31, englishTopics.size)
    assertEquals(31, gaTopics.size)

    // All should initially be "Not Started"
    val allNotStarted = revisions.all { it.status == "Not Started" }
    org.junit.Assert.assertTrue(allNotStarted)
  }

  @Test
  fun `verify TopicRevision model and RevisionStatus transitions`() {
    val topic = com.example.data.model.TopicRevision(
      id = 1L,
      dayNumber = 5,
      subject = "Quantitative Aptitude",
      topicTitle = "Time & Work LCM Method",
      status = "Not Started",
      notes = "Practice alternate day problems"
    )

    assertEquals("Not Started", topic.status)
    assertEquals(com.example.data.model.RevisionStatus.NOT_STARTED, topic.revisionStatus)

    val inReview = topic.copy(status = "Needs Review")
    assertEquals("Needs Review", inReview.status)
    assertEquals(com.example.data.model.RevisionStatus.NEEDS_REVIEW, inReview.revisionStatus)

    val mastered = inReview.copy(status = "Mastered")
    assertEquals("Mastered", mastered.status)
    assertEquals(com.example.data.model.RevisionStatus.MASTERED, mastered.revisionStatus)
  }
}
