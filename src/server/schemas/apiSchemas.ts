import { z } from 'zod';

export const GradeExamItemSchema = z.object({
  questionPrompt: z
    .string()
    .min(3, 'Question prompt is too short')
    .max(2000, 'Question prompt exceeds maximum length of 2000 characters')
    .transform((val: string) => val.trim()),
  studentAnswer: z
    .string()
    .min(1, 'Student answer cannot be empty')
    .max(5000, 'Student answer exceeds maximum length of 5000 characters')
    .transform((val: string) => val.trim()),
  modelAnswer: z
    .string()
    .min(1, 'Model answer is required')
    .max(3000, 'Model answer exceeds maximum length')
    .transform((val: string) => val.trim()),
  requiredKeywords: z.array(z.string().min(1).max(100)).default([]),
  maxScore: z.number().positive().max(100).default(2.0),
});

export const ExplainConceptSchema = z.object({
  topicTitle: z.string().min(1).max(200).transform((val: string) => val.trim()),
  question: z.string().min(3).max(1000).transform((val: string) => val.trim()),
  mode: z.enum(['technical', 'eli5']).default('technical'),
});

export type GradeExamItemInput = z.infer<typeof GradeExamItemSchema>;
export type ExplainConceptInput = z.infer<typeof ExplainConceptSchema>;
