import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

/// Service for interfacing with the Google Gemini API (Gemini 1.5 Flash).
class GeminiService {
  final String? _customApiKey;
  static const String _defaultEndpoint =
      'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent';

  GeminiService({String? apiKey}) : _customApiKey = apiKey;

  String get _apiKey {
    if (_customApiKey != null && _customApiKey!.isNotEmpty) {
      return _customApiKey!;
    }
    // Check compile-time environment variable if configured
    const fromEnv = String.fromEnvironment('GEMINI_API_KEY');
    return fromEnv;
  }

  bool get hasValidApiKey => _apiKey.isNotEmpty;

  /// Sends a prompt to the Gemini API and returns the generated text response.
  Future<String> generateText({
    required String prompt,
    String? systemInstruction,
    double temperature = 0.7,
  }) async {
    if (!hasValidApiKey) {
      throw StateError('Gemini API key is not configured.');
    }

    final uri = Uri.parse('$_defaultEndpoint?key=$_apiKey');
    final Map<String, dynamic> requestBody = {
      'contents': [
        {
          'parts': [
            {'text': prompt}
          ]
        }
      ],
      'generationConfig': {
        'temperature': temperature,
        'maxOutputTokens': 2048,
      }
    };

    if (systemInstruction != null && systemInstruction.isNotEmpty) {
      requestBody['systemInstruction'] = {
        'parts': [
          {'text': systemInstruction}
        ]
      };
    }

    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(requestBody),
    );

    if (response.statusCode != 200) {
      debugPrint('Gemini API Error (${response.statusCode}): ${response.body}');
      throw Exception('Gemini API failed with status code ${response.statusCode}: ${response.body}');
    }

    final data = jsonDecode(response.body) as Map<String, dynamic>;
    final candidates = data['candidates'] as List<dynamic>?;
    if (candidates == null || candidates.isEmpty) {
      throw Exception('Gemini returned no candidates.');
    }

    final content = candidates.first['content'] as Map<String, dynamic>?;
    final parts = content?['parts'] as List<dynamic>?;
    if (parts == null || parts.isEmpty) {
      throw Exception('Gemini candidate has no parts.');
    }

    return parts.first['text'] as String? ?? '';
  }
}
