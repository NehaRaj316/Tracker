package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RevisionCard
import com.example.data.model.RevisionData
import com.example.data.model.TopicRevision
import com.example.ui.components.AddTopicRevisionDialog
import com.example.ui.components.FormulaRevisionCard
import com.example.ui.components.SubjectRevisionRoadmapSummaryCard
import com.example.ui.components.TopicRevisionCard
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun RevisionScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier
) {
    val dayPlans by viewModel.dayPlans.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val topicRevisions by viewModel.topicRevisions.collectAsState()
    val masteredCount by viewModel.masteredTopicsCount.collectAsState()
    val needsReviewCount by viewModel.needsReviewTopicsCount.collectAsState()
    val notStartedCount by viewModel.notStartedTopicsCount.collectAsState()

    // 0: Daily Subject Revisions (Roadmap), 1: Formulas & Cheat Sheets, 2: Error Book
    var selectedMainTab by remember { mutableIntStateOf(0) }

    // Filters for Daily Subject Revisions
    var selectedDayFilter by remember { mutableIntStateOf(0) } // 0 = All Days, 1..31 = Specific Day
    var selectedSubjectFilter by remember { mutableStateOf("All") }
    var selectedStatusFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddTopicDialog by remember { mutableStateOf(false) }

    // Filtered Topic Revisions
    val filteredTopics = remember(topicRevisions, selectedDayFilter, selectedSubjectFilter, selectedStatusFilter, searchQuery) {
        topicRevisions.filter { topic ->
            val matchesDay = (selectedDayFilter == 0) || (topic.dayNumber == selectedDayFilter)
            val matchesSubject = (selectedSubjectFilter == "All") || (topic.subject.equals(selectedSubjectFilter, ignoreCase = true))
            val matchesStatus = (selectedStatusFilter == "All") || (topic.status.equals(selectedStatusFilter, ignoreCase = true))
            val matchesSearch = searchQuery.isBlank() ||
                topic.topicTitle.contains(searchQuery, ignoreCase = true) ||
                topic.notes.contains(searchQuery, ignoreCase = true) ||
                topic.subject.contains(searchQuery, ignoreCase = true)
            matchesDay && matchesSubject && matchesStatus && matchesSearch
        }
    }

    // Formulas Flashcards Filter
    val formulaSubjects = listOf("All Subjects", "Quantitative Aptitude", "Reasoning", "English", "General Awareness")
    var selectedFormulaSubjectTab by remember { mutableIntStateOf(0) }

    val formulaCards = remember(selectedFormulaSubjectTab, searchQuery) {
        val base = if (selectedFormulaSubjectTab == 0) {
            RevisionData.cards
        } else {
            RevisionData.cards.filter { it.subject == formulaSubjects[selectedFormulaSubjectTab] }
        }

        if (searchQuery.isBlank()) base
        else base.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.content.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            (it.formulaOrRule?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    // Errors & Daily Notes
    val mockErrors = remember(mockTests) {
        mockTests.filter { it.isAttempted && (it.errorAnalysis.isNotBlank() || it.weakTopics.isNotBlank()) }
    }

    val dayNotes = remember(dayPlans) {
        dayPlans.filter { it.userNotes.isNotBlank() }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("revision_screen_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Main Screen Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Rigorous Subject Revision Hub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Log Daily Subject Revisions • Mastered, Review & 31-Day Roadmap",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        // Top Navigation Tabs
        item {
            TabRow(
                selectedTabIndex = selectedMainTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = Color.Transparent,
                divider = {}
            ) {
                Tab(
                    selected = selectedMainTab == 0,
                    onClick = { selectedMainTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Checklist,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Roadmap Topics",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedMainTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedMainTab == 1,
                    onClick = { selectedMainTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Functions,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Formulas",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedMainTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedMainTab == 2,
                    onClick = { selectedMainTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Error Book",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedMainTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // TAB 0: Daily Subject Revisions (Roadmap)
        if (selectedMainTab == 0) {
            // Roadmap Revision Summary Card
            item {
                SubjectRevisionRoadmapSummaryCard(
                    totalTopics = topicRevisions.size,
                    masteredCount = masteredCount,
                    needsReviewCount = needsReviewCount,
                    notStartedCount = notStartedCount,
                    onLogNewTopicClick = { showAddTopicDialog = true }
                )
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    placeholder = { Text("Search topics, formulas or revision notes...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
            }

            // Status Filter Chips
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("All", "Mastered", "Needs Review", "Not Started").forEach { status ->
                            val isSelected = selectedStatusFilter == status
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedStatusFilter = status },
                                label = { Text(if (status == "All") "All Status" else status) },
                                leadingIcon = when (status) {
                                    "Mastered" -> {
                                        { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp)) }
                                    }
                                    "Needs Review" -> {
                                        { Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFEA580C), modifier = Modifier.size(16.dp)) }
                                    }
                                    "Not Started" -> {
                                        { Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp)) }
                                    }
                                    else -> null
                                }
                            )
                        }
                    }
                }
            }

            // Subject Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("All", "Quantitative Aptitude", "Reasoning", "English", "General Awareness").forEach { subj ->
                        val isSelected = selectedSubjectFilter == subj
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSubjectFilter = subj },
                            label = { Text(if (subj == "All") "All Subjects" else subj) }
                        )
                    }
                }
            }

            // Day Selector Filter Chips (1 to 31)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedDayFilter == 0) "Filter by Roadmap Day: All 31 Days" else "Filter by Roadmap Day: Day $selectedDayFilter",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (selectedDayFilter != 0) {
                            TextButton(
                                onClick = { selectedDayFilter = 0 },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Text("Reset to All Days", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedDayFilter == 0,
                            onClick = { selectedDayFilter = 0 },
                            label = { Text("All Days") }
                        )
                        (1..31).forEach { day ->
                            FilterChip(
                                selected = selectedDayFilter == day,
                                onClick = { selectedDayFilter = day },
                                label = { Text("Day $day") }
                            )
                        }
                    }
                }
            }

            // Topic Revision Items Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Roadmap Topics (${filteredTopics.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Tap status to update",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (filteredTopics.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No revision topics match this filter",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Try changing your day, subject or status filter, or tap 'Log Topic' to add one.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { showAddTopicDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Log Topic for Day ${if (selectedDayFilter > 0) selectedDayFilter else 1}")
                            }
                        }
                    }
                }
            } else {
                items(filteredTopics, key = { it.id }) { topic ->
                    TopicRevisionCard(
                        topic = topic,
                        onStatusChange = { newStatus ->
                            viewModel.updateTopicRevisionStatus(topic.id, newStatus)
                        },
                        onNotesChange = { notes ->
                            viewModel.updateTopicRevisionNotes(topic.id, notes)
                        },
                        onDelete = {
                            viewModel.deleteTopicRevision(topic)
                        }
                    )
                }
            }
        } else if (selectedMainTab == 1) {
            // TAB 1: Formulas Flashcards
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    placeholder = { Text("Search formulas, rules, idioms or GK...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedFormulaSubjectTab,
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent,
                    divider = {}
                ) {
                    formulaSubjects.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedFormulaSubjectTab == index,
                            onClick = { selectedFormulaSubjectTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (selectedFormulaSubjectTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(formulaCards) { card ->
                FormulaRevisionCard(card = card)
            }
        } else {
            // TAB 2: Error Book
            if (mockErrors.isEmpty() && dayNotes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Your Error Notebook is Empty",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "When you log mock test mistakes or write notes on your daily routine, they will automatically appear here for your nightly 9 PM revision block!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                if (mockErrors.isNotEmpty()) {
                    item {
                        Text(
                            text = "Mock Test Mistakes to Review (${mockErrors.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    items(mockErrors) { mock ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Mock #${mock.testNumber}: ${mock.provider}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Score: ${String.format("%.1f", mock.totalScore)}/200",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (mock.weakTopics.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Weak Topics: ${mock.weakTopics}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                if (mock.errorAnalysis.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = mock.errorAnalysis,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                if (dayNotes.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Daily Reflection Notes (${dayNotes.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    items(dayNotes) { plan ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Day ${plan.dayNumber}: ${plan.title}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = plan.userNotes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Topic Revision Dialog
    if (showAddTopicDialog) {
        AddTopicRevisionDialog(
            initialDayNumber = if (selectedDayFilter > 0) selectedDayFilter else 1,
            onDismiss = { showAddTopicDialog = false },
            onConfirm = { day, subject, title, status, notes ->
                viewModel.addTopicRevision(
                    dayNumber = day,
                    subject = subject,
                    topicTitle = title,
                    status = status,
                    notes = notes
                )
                showAddTopicDialog = false
            }
        )
    }
}
