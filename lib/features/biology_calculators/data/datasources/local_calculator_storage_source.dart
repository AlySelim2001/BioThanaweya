import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../../domain/entities/calculator_fsrs_item.dart';

/// Local offline storage for calculator history and FSRS spaced repetition items.
/// Guarantees zero network access and complete persistence across app restarts.
class LocalCalculatorStorageSource {
  static const String _prefKeyFsrsItems = 'PLACEHOLDER_KEY';
  static const String _prefKeyHistory = 'biothanaweya_calculator_history_v1';

  final List<CalculatorFsrsItem> _inMemoryFsrs = [];
  final List<Map<String, dynamic>> _inMemoryHistory = [];
  bool _isInitialized = false;

  Future<void> initialize() async {
    if (_isInitialized) return;

    try {
      final prefs = await SharedPreferences.getInstance();
      final rawFsrs = prefs.getString(_prefKeyFsrsItems);
      if (rawFsrs != null) {
        final List<dynamic> decoded = jsonDecode(rawFsrs);
        _inMemoryFsrs.clear();
        for (final item in decoded) {
          _inMemoryFsrs.add(CalculatorFsrsItem.fromMap(item as Map<String, dynamic>));
        }
      } else {
        // Pre-seed core Ministry formula review traps
        _inMemoryFsrs.addAll(_getPreseededFormulaItems());
        await _persistFsrs(prefs);
      }

      final rawHistory = prefs.getString(_prefKeyHistory);
      if (rawHistory != null) {
        final List<dynamic> decoded = jsonDecode(rawHistory);
        _inMemoryHistory.clear();
        for (final item in decoded) {
          _inMemoryHistory.add(Map<String, dynamic>.from(item as Map));
        }
      }
    } catch (_) {
      if (_inMemoryFsrs.isEmpty) {
        _inMemoryFsrs.addAll(_getPreseededFormulaItems());
      }
    }

    _isInitialized = true;
  }

  Future<List<CalculatorFsrsItem>> getFsrsItems() async {
    await initialize();
    return List.unmodifiable(_inMemoryFsrs);
  }

  Future<void> saveFsrsItem(CalculatorFsrsItem item) async {
    await initialize();
    final index = _inMemoryFsrs.indexWhere((f) => f.formulaId == item.formulaId);
    if (index != -1) {
      _inMemoryFsrs[index] = item;
    } else {
      _inMemoryFsrs.add(item);
    }
    final prefs = await SharedPreferences.getInstance();
    await _persistFsrs(prefs);
  }

  Future<List<Map<String, dynamic>>> getHistory() async {
    await initialize();
    return List.unmodifiable(_inMemoryHistory);
  }

  Future<void> addHistoryRecord(Map<String, dynamic> record) async {
    await initialize();
    _inMemoryHistory.insert(0, record);
    // Keep last 50 calculations
    if (_inMemoryHistory.length > 50) {
      _inMemoryHistory.removeRange(50, _inMemoryHistory.length);
    }
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_prefKeyHistory, jsonEncode(_inMemoryHistory));
  }

  Future<void> clearHistory() async {
    await initialize();
    _inMemoryHistory.clear();
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_prefKeyHistory);
  }

  Future<void> _persistFsrs(SharedPreferences prefs) async {
    await prefs.setString(
      _prefKeyFsrsItems,
      jsonEncode(_inMemoryFsrs.map((item) => item.toMap()).toList()),
    );
  }

  List<CalculatorFsrsItem> _getPreseededFormulaItems() {
    final now = DateTime.now();
    return [
      CalculatorFsrsItem(
        formulaId: 'trap_chargaff_turns',
        category: 'dna',
        titleAr: 'قاعدة اللفات التامة في DNA',
        formulaPromptAr: 'ما العلاقة بين أزواج القواعد واللفات التامة في لولب DNA المزدوج؟',
        nextReviewDate: now,
      ),
      CalculatorFsrsItem(
        formulaId: 'trap_h_zone_contraction',
        category: 'sarcomere',
        titleAr: 'المنطقة H في الانقباض التام',
        formulaPromptAr: 'كم يصبح عدد المناطق شبه المضيئة H عند حدوث انقباض عضلي تام؟',
        nextReviewDate: now,
      ),
      CalculatorFsrsItem(
        formulaId: 'trap_lethal_genes_ratio',
        category: 'genetics',
        titleAr: 'نسبة الأفراد الحية في الجينات المميتة',
        formulaPromptAr: 'ما هي النسبة المظهرية بين الأحياء عند تزاوج فردين هجينين لجينات مميتة؟',
        nextReviewDate: now,
      ),
      CalculatorFsrsItem(
        formulaId: 'trap_atp_aerobic_yield',
        category: 'respiration',
        titleAr: 'صافي جزيئات ATP في التنفس الهوائي',
        formulaPromptAr: 'كم جزيء ATP ينتج من أكسدة جزيء جلوكوز واحد أكسدة هوائية تامة؟',
        nextReviewDate: now,
      ),
    ];
  }
}
