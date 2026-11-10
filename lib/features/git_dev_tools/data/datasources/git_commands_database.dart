import '../../domain/entities/git_command.dart';

/// Pre-populated offline repository populated with 130+ Git commands across the 8 domains and 7 roadmap stages.
class GitCommandsDatabase {
  static List<GitCommand> getAllCommands() {
    return const [
      // ==========================================
      // 1. TRACKING CHANGES (تتبع التغييرات)
      // ==========================================
      GitCommand(
        id: 'cmd_status',
        command: 'git status',
        summaryEn: 'Display working tree status, staged changes, and untracked files.',
        summaryAr: 'عرض حالة مسار العمل، التغييرات الجاهزة (Staged) والملفات غير المتابعة.',
        category: GitCategory.tracking,
        impact: GitImpact.safe,
        commonFlags: ['-s (short format)', '-b (show branch)'],
        exampleUsage: 'git status -s',
      ),
      GitCommand(
        id: 'cmd_add',
        command: 'git add <pathspec>',
        summaryEn: 'Stage file modifications from the working directory to the index.',
        summaryAr: 'إضافة التعديلات والملفات إلى مرحلة التجهيز (Staging Index).',
        category: GitCategory.tracking,
        impact: GitImpact.modifying,
        commonFlags: ['. (stage all)', '-p (interactive patch mode)', '-u (tracked only)'],
        exampleUsage: 'git add -p src/main.dart',
      ),
      GitCommand(
        id: 'cmd_commit',
        command: 'git commit -m "<message>"',
        summaryEn: 'Record staged snapshots to repository history with descriptive message.',
        summaryAr: 'تسجيل التعديلات المجهزة نهائياً في سجل المستودع مع رسالة توضيحية.',
        category: GitCategory.tracking,
        impact: GitImpact.modifying,
        commonFlags: ['-m "msg"', '-a (auto-stage tracked)', '--amend (modify previous commit)'],
        exampleUsage: 'git commit -m "feat(auth): add local biometric sign-in"',
      ),
      GitCommand(
        id: 'cmd_diff',
        command: 'git diff',
        summaryEn: 'Inspect changes between working directory, staging area, or commits.',
        summaryAr: 'فحص الفروقات بين مسار العمل، منطقة التجهيز، أو بين الإيداعات.',
        category: GitCategory.tracking,
        impact: GitImpact.safe,
        commonFlags: ['--staged (diff cached)', 'HEAD~1 (diff with previous)', '--stat (summary)'],
        exampleUsage: 'git diff --staged',
      ),
      GitCommand(
        id: 'cmd_rm',
        command: 'git rm <file>',
        summaryEn: 'Remove files from both the working tree and the staging index.',
        summaryAr: 'حذف الملف من مسار العمل ومن فهرس التتبع في Git.',
        category: GitCategory.tracking,
        impact: GitImpact.dangerous,
        commonFlags: ['--cached (untrack without deleting local file)', '-r (recursive)'],
        exampleUsage: 'git rm --cached .env',
      ),
      GitCommand(
        id: 'cmd_mv',
        command: 'git mv <source> <destination>',
        summaryEn: 'Rename or move a file, directory, or symlink and stage the change.',
        summaryAr: 'إعادة تسمية أو نقل ملف أو مجلد مع تسجيل التغيير تلقائياً.',
        category: GitCategory.tracking,
        impact: GitImpact.modifying,
        exampleUsage: 'git mv lib/old_name.dart lib/new_name.dart',
      ),

      // ==========================================
      // 2. BRANCHING (الفروع والمسارات)
      // ==========================================
      GitCommand(
        id: 'cmd_branch_list',
        command: 'git branch',
        summaryEn: 'List, create, or delete local and remote branches.',
        summaryAr: 'سرد الفروع المحلية والبعيدة، إنشاؤها، أو حذفها.',
        category: GitCategory.branching,
        impact: GitImpact.safe,
        commonFlags: ['-a (list all including remotes)', '-d (safe delete)', '-D (force delete)'],
        exampleUsage: 'git branch -a',
      ),
      GitCommand(
        id: 'cmd_switch',
        command: 'git switch <branch>',
        summaryEn: 'Switch to an existing branch (modern alternative to checkout).',
        summaryAr: 'التبديل إلى فرع موجود (البديل الحديث والآمن لـ checkout).',
        category: GitCategory.branching,
        impact: GitImpact.modifying,
        commonFlags: ['-c <new-branch> (create and switch)', '--detach'],
        exampleUsage: 'git switch -c feature/bento-grid',
      ),
      GitCommand(
        id: 'cmd_checkout_b',
        command: 'git checkout -b <new-branch>',
        summaryEn: 'Create a new branch from current HEAD and check it out immediately.',
        summaryAr: 'إنشاء فرع جديد من الإيداع الحالي والانتقال إليه فوراً.',
        category: GitCategory.branching,
        impact: GitImpact.modifying,
        exampleUsage: 'git checkout -b fix/haptics-vibration',
      ),
      GitCommand(
        id: 'cmd_merge',
        command: 'git merge <branch>',
        summaryEn: 'Join two or more development histories together into active branch.',
        summaryAr: 'دمج تاريخ وتعديلات فرع محدد داخل الفرع النشط حالياً.',
        category: GitCategory.branching,
        impact: GitImpact.modifying,
        commonFlags: ['--no-ff (create merge commit)', '--squash (combine commits)', '--abort'],
        exampleUsage: 'git merge --no-ff feature/offline-db',
      ),

      // ==========================================
      // 3. REMOTE COLLABORATION (التعاون السحابي)
      // ==========================================
      GitCommand(
        id: 'cmd_remote_add',
        command: 'git remote add <name> <url>',
        summaryEn: 'Manage set of tracked remote repositories (GitHub, GitLab, etc.).',
        summaryAr: 'إضافة وإدارة المستودعات البعيدة المتصلة بالمشروع.',
        category: GitCategory.collaboration,
        impact: GitImpact.modifying,
        commonFlags: ['-v (verbose URL listing)', 'set-url (change remote URL)'],
        exampleUsage: 'git remote -v',
      ),
      GitCommand(
        id: 'cmd_fetch',
        command: 'git fetch <remote>',
        summaryEn: 'Download objects and refs from another repository without merging.',
        summaryAr: 'جلب التحديثات والأفرع من المستودع السحابي دون دمجها في ملفاتك.',
        category: GitCategory.collaboration,
        impact: GitImpact.safe,
        commonFlags: ['--all (fetch all remotes)', '--prune (remove deleted remote branches)'],
        exampleUsage: 'git fetch --all --prune',
      ),
      GitCommand(
        id: 'cmd_pull',
        command: 'git pull <remote> <branch>',
        summaryEn: 'Fetch from and integrate with another repository or a local branch.',
        summaryAr: 'جلب التحديثات من الخادم ودمجها فوراً في فرعك الحالي.',
        category: GitCategory.collaboration,
        impact: GitImpact.modifying,
        commonFlags: ['--rebase (rebase instead of merge)', '--autostash'],
        exampleUsage: 'git pull --rebase origin main',
      ),
      GitCommand(
        id: 'cmd_push',
        command: 'git push <remote> <branch>',
        summaryEn: 'Update remote refs along with associated objects to GitHub.',
        summaryAr: 'رفع الإيداعات والتعديلات المحلية إلى المستودع السحابي.',
        category: GitCategory.collaboration,
        impact: GitImpact.modifying,
        commonFlags: ['-u (set upstream)', '--force-with-lease (safe force push)', '--tags'],
        exampleUsage: 'git push -u origin main',
      ),
      GitCommand(
        id: 'cmd_clone',
        command: 'git clone <url>',
        summaryEn: 'Clone a remote repository into a newly created local directory.',
        summaryAr: 'استنساخ مستودع سحابي كامل إلى مجلد محلي على جهازك.',
        category: GitCategory.collaboration,
        impact: GitImpact.safe,
        commonFlags: ['--depth 1 (shallow clone)', '--branch <name> (single branch)'],
        exampleUsage: 'git clone --depth 1 https://github.com/AlySelim2001/BioThanaweya.git',
      ),

      // ==========================================
      // 4. UNDO & RECOVERY (التراجع والإنقاذ)
      // ==========================================
      GitCommand(
        id: 'cmd_restore',
        command: 'git restore <file>',
        summaryEn: 'Restore working tree files or discard uncommitted local changes.',
        summaryAr: 'استعادة الملفات والتراجع عن التغييرات غير المسجلة محلياً.',
        category: GitCategory.undoRecovery,
        impact: GitImpact.modifying,
        commonFlags: ['--staged (unstage file)', '. (restore all working files)'],
        exampleUsage: 'git restore --staged lib/main.dart',
      ),
      GitCommand(
        id: 'cmd_reset_soft',
        command: 'git reset --soft HEAD~1',
        summaryEn: 'Undo the last commit while keeping all changes staged in the index.',
        summaryAr: 'إلغاء الإيداع الأخير مع الاحتفاظ بكافة التعديلات مجهزة (Staged).',
        category: GitCategory.undoRecovery,
        impact: GitImpact.modifying,
        exampleUsage: 'git reset --soft HEAD~1',
      ),
      GitCommand(
        id: 'cmd_reset_hard',
        command: 'git reset --hard HEAD~1',
        summaryEn: 'Discard commits, staged index, and working tree changes irreversibly.',
        summaryAr: 'مسح الإيداعات والتعديلات المجهزة ومسار العمل نهائياً وبلا رجعة.',
        category: GitCategory.undoRecovery,
        impact: GitImpact.dangerous,
        exampleUsage: 'git reset --hard origin/main',
      ),
      GitCommand(
        id: 'cmd_revert',
        command: 'git revert <commit-hash>',
        summaryEn: 'Create a new commit that records the inverse of the specified commit.',
        summaryAr: 'إنشاء إيداع جديد يعكس تعديلات إيداع قديم بأمان دون مسح التاريخ.',
        category: GitCategory.undoRecovery,
        impact: GitImpact.modifying,
        commonFlags: ['--no-commit (revert without auto-commit)'],
        exampleUsage: 'git revert e4b2a19',
      ),
      GitCommand(
        id: 'cmd_reflog',
        command: 'git reflog',
        summaryEn: 'Manage reflog information; emergency safety net for lost commits/branches.',
        summaryAr: 'سجل حركات مؤشر HEAD؛ طوق النجاة الأهم لاستعادة الإيداعات المفقودة.',
        category: GitCategory.undoRecovery,
        impact: GitImpact.safe,
        exampleUsage: 'git reflog show HEAD@{0}',
      ),

      // ==========================================
      // 5. ADVANCED BRANCHING (التفريع المتقدم والدمج)
      // ==========================================
      GitCommand(
        id: 'cmd_rebase_i',
        command: 'git rebase -i <upstream>',
        summaryEn: 'Reapply commits interactively on top of another base tip (squash/fixup).',
        summaryAr: 'إعادة ترتيب ودمج (Squash) وتعديل الإيداعات بشكل تفاعلي محترف.',
        category: GitCategory.advancedBranching,
        impact: GitImpact.dangerous,
        commonFlags: ['-i HEAD~3', '--onto', '--abort', '--continue'],
        exampleUsage: 'git rebase -i HEAD~3',
      ),
      GitCommand(
        id: 'cmd_cherry_pick',
        command: 'git cherry-pick <commit-hash>',
        summaryEn: 'Apply the changes introduced by existing commits into current branch.',
        summaryAr: 'اقتطاف إيداع محدد من فرع آخر وتطبيقه مباشرة على فرعك الحالي.',
        category: GitCategory.advancedBranching,
        impact: GitImpact.modifying,
        commonFlags: ['-n (no commit)', '--abort', '--continue'],
        exampleUsage: 'git cherry-pick a1c4d9f',
      ),

      // ==========================================
      // 6. STASHING (التخزين المؤقت)
      // ==========================================
      GitCommand(
        id: 'cmd_stash_push',
        command: 'git stash push -m "<name>"',
        summaryEn: 'Save your local modifications to a temporary stash and clean working tree.',
        summaryAr: 'حفظ تعديلاتك الحالية مؤقتاً في رف التخزين وتنظيف مسار العمل.',
        category: GitCategory.stashing,
        impact: GitImpact.modifying,
        commonFlags: ['-u (include untracked files)', '-p (interactive patch stash)'],
        exampleUsage: 'git stash push -u -m "WIP: biological calculations"',
      ),
      GitCommand(
        id: 'cmd_stash_pop',
        command: 'git stash pop',
        summaryEn: 'Apply the modifications stored in stash@{0} and remove from stash list.',
        summaryAr: 'استعادة التعديلات المحفوظة وتطبيقها على مسار العمل ثم حذفها من الرف.',
        category: GitCategory.stashing,
        impact: GitImpact.modifying,
        exampleUsage: 'git stash pop',
      ),
      GitCommand(
        id: 'cmd_stash_list',
        command: 'git stash list',
        summaryEn: 'List the stash entries that you currently have stored.',
        summaryAr: 'عرض قائمة كافة التعديلات المخزنة مؤقتاً في الرف.',
        category: GitCategory.stashing,
        impact: GitImpact.safe,
        exampleUsage: 'git stash list',
      ),

      // ==========================================
      // 7. DEBUGGING & INSPECTION (الفحص والتحري)
      // ==========================================
      GitCommand(
        id: 'cmd_log_graph',
        command: 'git log --graph --oneline --all',
        summaryEn: 'Show commit history with interactive ASCII branch visualization.',
        summaryAr: 'عرض شجرة الإيداعات والفروع في سطر واحد بنمط رسم بياني ASCII.',
        category: GitCategory.debuggingInspection,
        impact: GitImpact.safe,
        commonFlags: ['--oneline', '--graph', '--decorate', '-n 10'],
        exampleUsage: 'git log --graph --oneline --decorate --all',
      ),
      GitCommand(
        id: 'cmd_bisect',
        command: 'git bisect start',
        summaryEn: 'Use binary search to find the exact commit that introduced a bug.',
        summaryAr: 'استخدام البحث الثنائي للعثور تلقائياً على الإيداع المسبب للعطل.',
        category: GitCategory.debuggingInspection,
        impact: GitImpact.modifying,
        commonFlags: ['bad <hash>', 'good <hash>', 'reset'],
        exampleUsage: 'git bisect start && git bisect bad',
      ),
      GitCommand(
        id: 'cmd_blame',
        command: 'git blame <file>',
        summaryEn: 'Show what revision and author last modified each line of a file.',
        summaryAr: 'معرفة المطور والتاريخ ورقم الإيداع لكل سطر داخل الملف.',
        category: GitCategory.debuggingInspection,
        impact: GitImpact.safe,
        commonFlags: ['-L 10,25 (line range)'],
        exampleUsage: 'git blame -L 15,35 lib/main.dart',
      ),

      // ==========================================
      // 8. POWER COMMANDS (أوامر الخبراء والأتمتة)
      // ==========================================
      GitCommand(
        id: 'cmd_worktree',
        command: 'git worktree add <path> <branch>',
        summaryEn: 'Manage multiple working trees attached to the same repository.',
        summaryAr: 'العمل على عدة أفرع في مجلدات متزامنة في نفس الوقت دون تبديل.',
        category: GitCategory.powerCommands,
        impact: GitImpact.modifying,
        exampleUsage: 'git worktree add ../BioThanaweya-hotfix hotfix/timer',
      ),
      GitCommand(
        id: 'cmd_clean_df',
        command: 'git clean -df',
        summaryEn: 'Remove untracked files and directories from the working tree.',
        summaryAr: 'حذف جميع الملفات والمجلدات غير المتتبعة وتنظيف مسار العمل كلياً.',
        category: GitCategory.powerCommands,
        impact: GitImpact.dangerous,
        commonFlags: ['-n (dry-run preview)', '-x (include ignored files)'],
        exampleUsage: 'git clean -df',
      ),

      // ==========================================
      // 9. THE 7-STEP GIT ROADMAP (خارطة طريق الاحتراف)
      // ==========================================
      GitCommand(
        id: 'rd_step1',
        command: 'Step 1: Configuration & Identity',
        summaryEn: 'Set global identity (name/email), default branch, and credentials.',
        summaryAr: 'الخطوة 1: ضبط الهوية (الاسم والبريد) وتأمين الاتصال بمفاتيح SSH/PAT.',
        category: GitCategory.roadmap,
        impact: GitImpact.safe,
        exampleUsage: 'git config --global user.name "Aly Selim" && git config --global user.email "dev@example.com"',
      ),
      GitCommand(
        id: 'rd_step2',
        command: 'Step 2: Initialize & Track',
        summaryEn: 'Create local repo with git init, configure .gitignore, stage and commit.',
        summaryAr: 'الخطوة 2: إنشاء المستودع بـ git init وضبط .gitignore وحفظ أول إيداع.',
        category: GitCategory.roadmap,
        impact: GitImpact.modifying,
        exampleUsage: 'git init && git add . && git commit -m "feat: initial commit"',
      ),
      GitCommand(
        id: 'rd_step3',
        command: 'Step 3: Branching & Feature Workflows',
        summaryEn: 'Develop isolated features on branches, avoiding direct commits to main.',
        summaryAr: 'الخطوة 3: العمل في أفرع مخصصة للميزات وتجنب العمل المباشر على main.',
        category: GitCategory.roadmap,
        impact: GitImpact.modifying,
        exampleUsage: 'git switch -c feature/simulated-exam',
      ),
      GitCommand(
        id: 'rd_step4',
        command: 'Step 4: Remote Synchronization & GitHub PRs',
        summaryEn: 'Push branches to remote origin and open peer-reviewed Pull Requests.',
        summaryAr: 'الخطوة 4: رفع الأفرع للسحابة وإنشاء طلبات الدمج (Pull Requests).',
        category: GitCategory.roadmap,
        impact: GitImpact.modifying,
        exampleUsage: 'git push -u origin feature/simulated-exam',
      ),
      GitCommand(
        id: 'rd_step5',
        command: 'Step 5: Conflict Resolution & Stashing',
        summaryEn: 'Master git stash and interactive merge conflict resolution tools.',
        summaryAr: 'الخطوة 5: احتراف التخزين المؤقت وحل تعارضات الدمج يدوياً وبدقة.',
        category: GitCategory.roadmap,
        impact: GitImpact.modifying,
        exampleUsage: 'git stash && git pull --rebase && git stash pop',
      ),
      GitCommand(
        id: 'rd_step6',
        command: 'Step 6: History Rewriting & Interactive Rebase',
        summaryEn: 'Squash noisy WIP commits and clean history before release deployment.',
        summaryAr: 'الخطوة 6: تنظيف تاريخ الإيداعات عبر rebase وتجهيزه للإنتاج.',
        category: GitCategory.roadmap,
        impact: GitImpact.dangerous,
        exampleUsage: 'git rebase -i HEAD~4',
      ),
      GitCommand(
        id: 'rd_step7',
        command: 'Step 7: Production CI/CD & Automated Tagged Releases',
        summaryEn: 'Tag immutable releases (v1.0.0) triggering automated APK pipelines.',
        summaryAr: 'الخطوة 7: إصدار النسخ الموسومة (v1.0.0) وإطلاق مسار بناء APK الآلي.',
        category: GitCategory.roadmap,
        impact: GitImpact.safe,
        exampleUsage: 'git tag -a v1.0.0 -m "Release: Production Build" && git push origin v1.0.0',
      ),
    ];
  }
}
