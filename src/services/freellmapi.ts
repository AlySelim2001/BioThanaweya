/**
 * FreeLLMAPI Integration Module for BioThanaweya
 * 
 * Models the FreeLLMAPI pattern (https://github.com/tashfeenahmed/freellmapi, MIT)
 * Provides a configurable, self-hostable OpenAI-compatible AI client/service module
 * with:
 * - Configurable base URL (e.g., local/self-hosted router endpoint)
 * - API key management
 * - Provider failover/fallback handling
 * - Built-in deterministic offline biology study/explanation fallback
 * - 100% offline capability without hard failures or paid API requirements
 */

import { config } from '../server/config/env';

/**
 * FreeLLMAPI Provider Configuration
 */
export type FreeLLMAPIProvider = 'openai' | 'anthropic' | 'gemini' | 'local' | 'freellmapi';

export interface FreeLLMAPIConfig {
  provider?: FreeLLMAPIProvider;
  apiKey?: string;
  baseUrl?: string;
  model?: string;
  timeoutMs?: number;
  enabled?: boolean;
}

/**
 * Default FreeLLMAPI configuration - self-hosted/local first
 */
export const DEFAULT_FREEllMAPI_CONFIG: FreeLLMAPIConfig = {
  provider: 'local',
  baseUrl: process.env.FREE_LLMAPI_BASE_URL || 'http://localhost:8080',
  model: 'phi-3',
  timeoutMs: 30000,
  enabled: true,
};

/**
 * FreeLLMAPI Service Class
 * Provides OpenAI-compatible fetch with provider routing and fallback
 */
export class FreeLLMAPIService {
  private config: FreeLLMAPIConfig;
  private provider: FreeLLMAPIProvider;
  private fallbackChain: FreeLLMAPIProvider[] = [
    'local',    // 1. Local/self-hosted (offline first)
    'gemini',   // 2. Google Gemini (free tier)
    'anthropic',// 3. Anthropic (free tier when available)
    'openai',   // 4. OpenAI-compatible (when configured)
  ];
  private currentIndex: number = 0;

  constructor(userConfig: FreeLLMAPIConfig = {}) {
    this.config = { ...DEFAULT_FREEllMAPI_CONFIG, ...userConfig };
    this.provider = this.config.provider || 'local';
  }

  /**
   * Set the provider configuration and reset to first enabled provider
   */
  setProvider(provider: FreeLLMAPIProvider, config: FreeLLMAPIConfig = {}): void {
    this.provider = provider;
    // Reset fallback chain starting from the selected provider
    const providerIndex = this.fallbackChain.indexOf(provider);
    this.currentIndex = providerIndex >= 0 ? providerIndex : 0;
  }

  /**
   * Get the current provider
   */
  getProvider(): FreeLLMAPIProvider {
    return this.provider;
  }

  /**
   * Get current configuration
   */
  getConfig(): FreeLLMAPIConfig {
    return this.config;
  }

  /**
   * Determine if we're in offline/local mode
   */
  isOfflineMode(): boolean {
    return this.provider === 'local' || !this.config.enabled;
  }

  /**
   * Generate a deterministic biology fallback explanation
   * This ensures 100% offline capability without any external API dependence
   */
  private generateBiologyFallback(topic: string, mode: 'eli5' | 'technical'): string {
    const ar: Record<'eli5' | 'technical', Record<string, string>> = {
      eli5: {
        dna: 'DNA هيBlueprint الحياة. كل خلية في جسمك تحمل كود DNA طوله 2 متر مطوَّر في نواة الخلية. think of it as a huge book of instructions that fits inside a tiny cell! المبدأ نفسه: الحمض النووي هو كتاب تعليمات ضخم (2 متر) يوضع داخل خلية صغيرة جداً..',
        protein: 'البروتينات هي العمال في جسمك. DNA ترسل الرسالة للريبوسوم الذي يصنع البروتينات مثل العصابات في العضلة. تماماً كطلب pizza: الطلب يصنع عند الحاجة!',
        cell: 'الخلية هي المصنع الصغرى.DNA تقول للمصنع ما يصنعه، والغشاء يسيطر على ما يدخل ويخرج.就像工厂：DNA指示工厂生产什么，膜控制进出。',
        immunity: 'نظام المناعة يكافح الأمراض مثل الجيش. الخلايا البيضاء هي الجنود التي تبحث عن البكتيريا وتدميرها. عندما تصاب بنزلة برد، الجيش الداخلي ينشط لمحاربة الفيروسات.',
      },
      technical: {
        dna: 'DNA replication follows semiconservative replication model. Helicase unwinds the double helix at replication forks. DNA Polymerase III synthesizes leading strand continuously and lagging strand in Okazaki fragments. DNA Ligase seals Okazaki fragments. Primase synthesizes RNA primers providing 3\'-OH group required by DNA Polymerase. Chargaff rules: A=T, G=C, purines=50%.',
        protein: 'Translation occurs on ribosomes in cytoplasm. mRNA codons are read by tRNA anticodons. Each amino acid is specified by a 3-nucleotide codon. The genetic code is degenerate (multiple codons per amino acid) and universal across almost all organisms. Stop codons (UAA, UAG, UGA) terminate translation. Peptide bonds link amino acids via dehydration synthesis.',
        cell: 'Cell membrane is a phospholipid bilayer with embedded proteins. Transport mechanisms include passive diffusion, facilitated diffusion via carrier proteins, active transport using ATP-driven pumps (Na+/K+ ATPase), and vesicular transport (endocytosis/exocytosis). Signal transduction cascades amplify cellular responses.',
        immunity: 'Adaptive immunity involves B cells producing antibodies and T cells mediating cellular immunity. Antigen presentation via MHC class I/II molecules activates CD8+ cytotoxic T cells and CD4+ helper T cells. Cytokine signaling coordinates immune response. Memory cells provide long-term protection.',
      },
    };

    const arTopic = ar[mode]?.[topic.toLowerCase()] || ar[mode]?.dna || '';
    if (arTopic) {
      return `${arTopic} (Arabic ${mode} fallback)`;
    }

    // English fallback
    const enFallback: Record<'eli5' | 'technical', Record<string, string>> = {
      eli5: {
        dna: 'DNA is the instruction manual for life. Every cell in your body contains about 2 meters of DNA tightly packed into the nucleus. Think of it as a huge book of instructions that fits inside a tiny cell!',
        protein: 'Proteins are the workers in your body. DNA sends a message to the ribosome which makes proteins like the muscle fibers. It\'s like ordering a pizza: the order is placed when needed!',
        cell: 'The cell is a miniature factory. DNA tells the factory what to make, and the membrane controls what goes in and out. Like a factory: DNA instructs the factory what to produce, and the membrane controls what enters and exits.',
        immunity: 'The immune system fights disease like an army. White blood cells are the soldiers that seek out and destroy bacteria. When you catch a cold, your internal army activates to fight off the viruses.',
      },
      technical: {
        dna: 'DNA replication follows a semiconservative replication model. Helicase unwinds the double helix at replication forks. DNA Polymerase III synthesizes the leading strand continuously and the lagging strand in Okazaki fragments. DNA Ligase seals the Okazaki fragments. Primase synthesizes RNA primers providing the essential 3\'-OH group required by DNA Polymerase. Chargaff\'s rules apply: A=T, G=C, and purines make up 50% of bases.',
        protein: 'Translation occurs on ribosomes in the cytoplasm. mRNA codons are read by tRNA anticodons. Each amino acid is specified by a 3-nucleotide codon. The genetic code is degenerate (multiple codons per amino acid) and universal across almost all organisms. Stop codons (UAA, UAG, UGA) terminate translation. Peptide bonds link amino acids via dehydration synthesis.',
        cell: 'The cell membrane is a phospholipid bilayer with embedded proteins. Transport mechanisms include passive diffusion, facilitated diffusion via carrier proteins, active transport using ATP-driven pumps (Na+/K+ ATPase), and vesicular transport (endocytosis/exocytosis). Signal transduction cascades amplify cellular responses.',
        immunity: 'Adaptive immunity involves B cells producing antibodies and T cells mediating cellular immunity. Antigen presentation via MHC class I/II molecules activates CD8+ cytotoxic T cells and CD4+ helper T cells. Cytokine signaling coordinates the immune response. Memory cells provide long-term protection.',
      },
    };

    const enTopic = enFallback[mode]?.[topic.toLowerCase()] || enFallback[mode]?.dna || '';
    if (enTopic) {
      return `English ${mode} fallback: ${enTopic}`;
    }

    // Absolute last resort
    return mode === 'eli5'
      ? `Biology ${mode} explanation: ${topic} is a biological concept covered in the Egyptian Thanaweya Amma curriculum.`
      : `Biology ${mode} explanation for ${topic}: This follows the official Ministry syllabus with precise mechanisms, enzymes, and ion references.`;
  }

  /**
   * Generate AI explanation with full fallback chain
   */
  async generateExplanation(
    topic: string,
    question: string,
    mode: 'eli5' | 'technical' = 'eli5'
  ): Promise<{ success: boolean; text: string; fromFallback: boolean }> {
    // If offline or local mode, use deterministic fallback immediately
    if (this.isOfflineMode()) {
      return {
        success: true,
        text: this.generateBiologyFallback(topic, mode),
        fromFallback: true,
      };
    }

    // Try providers in fallback chain
    for (let i = this.currentIndex; i < this.fallbackChain.length; i++) {
      const provider = this.fallbackChain[i];
      try {
        const result = await this.tryProviderExplain(provider, topic, question, mode);
        if (result.success) {
          // Update current index to this provider for next time
          this.currentIndex = i;
          return {
            success: true,
            text: result.text,
            fromFallback: false,
          };
        }
      } catch (error) {
        console.warn(`FreeLLMAPI provider ${provider} failed, trying next:`, error);
        // Continue to next provider
        this.currentIndex = i + 1;
        continue;
      }
    }

    // All providers failed - use deterministic fallback
    return {
      success: true,
      text: this.generateBiologyFallback(topic, mode),
      fromFallback: true,
    };
  }

  private async tryProviderExplain(
    provider: FreeLLMAPIProvider,
    topic: string,
    question: string,
    mode: 'eli5' | 'technical'
  ): Promise<{ success: boolean; text: string }> {
    switch (provider) {
      case 'local': {
        // Try local self-hosted router at configured base URL
        if (!this.config.baseUrl) {
          throw new Error('No base URL configured for local provider');
        }
        const response = await fetch(`${this.config.baseUrl}/v1/chat/completions`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            ...(this.config.apiKey ? { 'Authorization': `Bearer ${this.config.apiKey}` } : {}),
          },
          body: JSON.stringify({
            model: this.config.model || 'phi-3',
            messages: [
              { role: 'system', content: `You are a biology educator for Egyptian Thanaweya Amma curriculum. Explain in ${mode} mode.` },
              { role: 'user', content: question },
            ],
            temperature: 0.7,
          }),
          signal: AbortSignal.timeout(this.config.timeoutMs || 30000),
        });

        if (!response.ok) {
          const errText = await response.text();
          throw new Error(`Local provider failed: ${response.status} - ${errText}`);
        }

        const data = await response.json();
        return {
          success: true,
          text: data.choices?.[0]?.message?.content || 'No response from local AI',
        };
      }

      case 'gemini': {
        // Use existing Gemini integration as fallback
        if (!config.geminiApiKey) {
          throw new Error('No GEMINI_API_KEY configured');
        }
        // Reuse the existing AiExaminerService pattern
        const { GoogleGenAI } = await import('@google/genai');
        const ai = new GoogleGenAI({ apiKey: config.geminiApiKey });
        const model = ai.models.generateContent({
          model: 'gemini-3.8-flash',
          contents: question,
        });
        const response = await model;
        return {
          success: true,
          text: response.text || 'No response from Gemini',
        };
      }

      case 'anthropic': {
        if (!this.config.apiKey) {
          throw new Error('No Anthropic API key configured');
        }
        const response = await fetch('https://api.anthropic.com/v1/messages', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'x-api-key': this.config.apiKey,
            'anthropic-version': '2023-06-01',
          },
          body: JSON.stringify({
            model: this.config.model || 'claude-3-haiku-20240307',
            max_tokens: 1024,
            messages: [
              { role: 'user', content: question },
            ],
          }),
          signal: AbortSignal.timeout(this.config.timeoutMs || 30000),
        });

        if (!response.ok) {
          const errText = await response.text();
          throw new Error(`Anthropic provider failed: ${response.status} - ${errText}`);
        }

        const data = await response.json();
        return {
          success: true,
          text: data.content?.[0]?.text || 'No response from Anthropic',
        };
      }

      case 'openai': {
        if (!this.config.apiKey) {
          throw new Error('No OpenAI API key configured');
        }
        const response = await fetch('https://api.openai.com/v1/chat/completions', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.config.apiKey}`,
          },
          body: JSON.stringify({
            model: this.config.model || 'gpt-3.5-turbo',
            messages: [
              { role: 'system', content: `You are a biology educator for Egyptian Thanaweya Amma curriculum.` },
              { role: 'user', content: question },
            ],
            temperature: 0.7,
          }),
          signal: AbortSignal.timeout(this.config.timeoutMs || 30000),
        });

        if (!response.ok) {
          const errText = await response.text();
          throw new Error(`OpenAI provider failed: ${response.status} - ${errText}`);
        }

        const data = await response.json();
        return {
          success: true,
          text: data.choices?.[0]?.message?.content || 'No response from OpenAI',
        };
      }

      default:
        throw new Error(`Unknown provider: ${provider}`);
    }
  }

  /**
   * Grade an exam answer with full AI fallback chain
   */
  async gradeExamAnswer(
    questionPrompt: string,
    studentAnswer: string,
    modelAnswer: string,
    requiredKeywords: string[] = []
  ): Promise<{ success: boolean; gradeStatus: string; score: number; feedback: string }> {
    // If offline or no AI configured, use local grading engine
    if (this.isOfflineMode() || requiredKeywords.length === 0) {
      const detected = requiredKeywords.filter(kw => 
        studentAnswer.toLowerCase().includes(kw.toLowerCase())
      );
      const matchRatio = requiredKeywords.length > 0 
        ? detected.length / requiredKeywords.length 
        : 1;

      let gradeStatus = 'NEEDS_REVISION';
      let score = 0;
      let feedback = '';

      if (matchRatio >= 0.85) {
        gradeStatus = 'FULL_MARK';
        score = 2.0;
        feedback = 'Excellent! Response adheres to mark scheme criteria.';
      } else if (matchRatio >= 0.5) {
        gradeStatus = 'PARTIAL_CREDIT';
        score = 1.5;
        feedback = 'Good attempt but missing critical Ministry keywords.';
      } else {
        gradeStatus = 'NEEDS_REVISION';
        score = 0.5;
        feedback = 'Missing essential scientific terms.';
      }

      if (detected.length > 0) {
        feedback += ` Detected keywords: ${detected.join(', ')}`;
      }

      return {
        success: true,
        gradeStatus,
        score,
        feedback,
      };
    }

    // Try providers in fallback chain
    for (let i = this.currentIndex; i < this.fallbackChain.length; i++) {
      const provider = this.fallbackChain[i];
      try {
        const result = await this.tryProviderGrade(provider, questionPrompt, studentAnswer, modelAnswer, requiredKeywords);
        if (result.success) {
          this.currentIndex = i;
          return {
            success: true,
            gradeStatus: result.gradeStatus,
            score: result.score,
            feedback: result.feedback,
          };
        }
      } catch (error) {
        console.warn(`FreeLLMAPI grading provider ${provider} failed, trying next:`, error);
        this.currentIndex = i + 1;
        continue;
      }
    }

    // All providers failed - use local grading
    return this.gradeExamAnswer(
      questionPrompt, studentAnswer, modelAnswer, requiredKeywords
    );
  }

  private async tryProviderGrade(
    provider: FreeLLMAPIProvider,
    questionPrompt: string,
    studentAnswer: string,
    modelAnswer: string,
    requiredKeywords: string[]
  ): Promise<{ success: boolean; gradeStatus: string; score: number; feedback: string }> {
    switch (provider) {
      case 'local': {
        // Deterministic local grading
        const cleanAnswer = studentAnswer.trim().toLowerCase();
        const totalRequired = Math.max(1, requiredKeywords.length);
        const detectedKeywords: string[] = [];

        for (const kw of requiredKeywords) {
          if (cleanAnswer.includes(kw.toLowerCase())) {
            detectedKeywords.push(kw);
          }
        }

        const matchRatio = detectedKeywords.length / totalRequired;
        let gradeStatus: string;
        let score: number;
        let feedback: string;

        if (matchRatio >= 0.85) {
          gradeStatus = 'FULL_MARK';
          score = 2.0;
          feedback = 'Excellent! High-scoring response fully adhering to the Egyptian Ministry mark scheme.';
        } else if (matchRatio >= 0.5) {
          gradeStatus = 'PARTIAL_CREDIT';
          score = 1.5;
          feedback = 'Good attempt, but missing critical Ministry mark scheme keywords.';
        } else if (detectedKeywords.length > 0) {
          gradeStatus = 'PARTIAL_CREDIT';
          score = 0.5;
          feedback = `Partial credit. Detected keywords: ${detectedKeywords.join(', ')}. ${studentAnswer.substring(0, 100)}...`;
        } else {
          gradeStatus = 'NEEDS_REVISION';
          score = 0.0;
          feedback = 'Sub-standard response. Missing essential scientific terms.';
        }

        // Add penalty warning if keywords missing
        const missingKeywords = requiredKeywords.filter(kw => !detectedKeywords.includes(kw));
        if (missingKeywords.length > 0) {
          feedback += `\n\nPenalty Warning: Egyptian Ministry examiners would deduct marks for missing: ${missingKeywords.join(', ')}.`;
        }

        return { success: true, gradeStatus, score, feedback };
      }

      case 'gemini': {
        if (!config.geminiApiKey) throw new Error('No GEMINI_API_KEY');
        const { GoogleGenAI } = await import('@google/genai');
        const ai = new GoogleGenAI({ apiKey: config.geminiApiKey });
        const prompt = `Evaluate student answer for Egyptian Thanaweya Amma Biology:\nQuestion: ${questionPrompt}\nModel Answer: ${modelAnswer}\nStudent Answer: ${studentAnswer}\nRequired Keywords: ${JSON.stringify(requiredKeywords)}\nOutput JSON: {"scoreAwarded": number, "maxScore": number, "gradeStatus": "FULL_MARK"|"PARTIAL_CREDIT"|"NEEDS_REVISION", "examinerFeedback": string}`;

        const model = ai.models.generateContent({
          model: 'gemini-3.8-flash',
          contents: prompt,
          config: { responseMimeType: 'application/json' },
        });
        const response = await model;
        const data = JSON.parse(response.text || '{}');
        return {
          success: true,
          gradeStatus: data.gradeStatus || 'NEEDS_REVISION',
          score: typeof data.scoreAwarded === 'number' ? data.scoreAwarded : 0,
          feedback: data.examinerFeedback || 'Evaluated by Gemini AI',
        };
      }

      case 'anthropic': {
        if (!this.config.apiKey) throw new Error('No Anthropic key');
        const prompt = `Evaluate student answer for Egyptian Thanaweya Amma Biology:\nQuestion: ${questionPrompt}\nModel Answer: ${modelAnswer}\nStudent Answer: ${studentAnswer}\nRequired Keywords: ${JSON.stringify(requiredKeywords)}\nOutput JSON: {"scoreAwarded": number, "maxScore": number, "gradeStatus": "FULL_MARK"|"PARTIAL_CREDIT"|"NEEDS_REVISION", "examinerFeedback": string}`;

        const response = await fetch('https://api.anthropic.com/v1/messages', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'x-api-key': this.config.apiKey!,
            'anthropic-version': '2023-06-01',
          },
          body: JSON.stringify({
            model: 'claude-3-haiku-20240307',
            max_tokens: 1024,
            messages: [{ role: 'user', content: prompt }],
          }),
          signal: AbortSignal.timeout(30000),
        });

        if (!response.ok) throw new Error('Anthropic grading failed');
        const data = await response.json();
        return {
          success: true,
          gradeStatus: data.content?.[0]?.text?.includes('FULL_MARK') ? 'FULL_MARK' : 
                       data.content?.[0]?.text?.includes('PARTIAL') ? 'PARTIAL_CREDIT' : 'NEEDS_REVISION',
          score: 2.0,
          feedback: data.content?.[0]?.text || 'Evaluated by Anthropic AI',
        };
      }

      case 'openai': {
        if (!this.config.apiKey) throw new Error('No OpenAI key');
        const prompt = `Evaluate student answer for Egyptian Thanaweya Amma Biology:\nQuestion: ${questionPrompt}\nModel Answer: ${modelAnswer}\nStudent Answer: ${studentAnswer}\nRequired Keywords: ${JSON.stringify(requiredKeywords)}\nOutput JSON: {"scoreAwarded": number, "maxScore": number, "gradeStatus": "FULL_MARK"|"PARTIAL_CREDIT"|"NEEDS_REVISION", "examinerFeedback": string}`;

        const response = await fetch('https://api.openai.com/v1/chat/completions', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.config.apiKey}`,
          },
          body: JSON.stringify({
            model: 'gpt-3.5-turbo',
            messages: [{ role: 'user', content: prompt }],
            temperature: 0,
          }),
          signal: AbortSignal.timeout(30000),
        });

        if (!response.ok) throw new Error('OpenAI grading failed');
        const data = await response.json();
        const parsed = JSON.parse(data.choices?.[0]?.message?.content || '{}');
        return {
          success: true,
          gradeStatus: parsed.gradeStatus || 'NEEDS_REVISION',
          score: typeof parsed.scoreAwarded === 'number' ? parsed.scoreAwarded : 0,
          feedback: parsed.examinerFeedback || 'Evaluated by OpenAI',
        };
      }

      default:
        throw new Error(`Unknown provider for grading: ${provider}`);
    }
  }
}

/**
 * Export singleton instance with default config
 */
export const freeLLMAPI = new FreeLLMAPIService();

/**
 * Convenience hook for React components
 */
export type UseFreeLLMAPIReturn = {
  provider: FreeLLMAPIProvider;
  isOffline: boolean;
  generateExplanation: (topic: string, question: string, mode: 'eli5' | 'technical') => Promise<{ success: boolean; text: string; fromFallback: boolean }>;
  gradeExamAnswer: (questionPrompt: string, studentAnswer: string, modelAnswer: string, requiredKeywords?: string[]) => Promise<{ success: boolean; gradeStatus: string; score: number; feedback: string }>;
};

export function useFreeLLMAPI(): UseFreeLLMAPIReturn {
  return {
    provider: freeLLMAPI.getProvider(),
    isOffline: freeLLMAPI.isOfflineMode(),
    generateExplanation: freeLLMAPI.generateExplanation.bind(freeLLMAPI),
    gradeExamAnswer: freeLLMAPI.gradeExamAnswer.bind(freeLLMAPI),
  };
}