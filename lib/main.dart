import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'core/constants/curriculum_data.dart';
import 'core/theme/app_theme.dart';
import 'features/achievements/presentation/screens/achievements_screen.dart';
import 'features/biology_agents/presentation/screens/topic_detail_screen.dart';
import 'features/biology_agents/presentation/widgets/sparkline_chart.dart';
import 'features/exam_simulator/presentation/screens/exam_simulation_screen.dart';
import 'features/flashcards/presentation/screens/flashcard_screen.dart';
import 'features/interactive_suite/bound_bio_app_suite.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(ProviderScope(child: BoundBioAppRoot()));
}
