import 'dart:math';
import 'package:flutter/material.dart';

enum ConfettiShape { rectangle, circle, diamond }

class _ConfettiParticle {
  double x;
  double y;
  double vx;
  double vy;
  double rotation;
  double rotationSpeed;
  double size;
  Color color;
  ConfettiShape shape;

  _ConfettiParticle({
    required this.x,
    required this.y,
    required this.vx,
    required this.vy,
    required this.rotation,
    required this.rotationSpeed,
    required this.size,
    required this.color,
    required this.shape,
  });
}

/// ودجت احتفالي عالي الأداء لتوليد جزيئات الكونفيتي (Confetti Particle Animation)
class ConfettiParticleWidget extends StatefulWidget {
  final Widget child;
  final bool autoStart;
  final Duration duration;

  const ConfettiParticleWidget({
    Key? key,
    required this.child,
    this.autoStart = true,
    this.duration = const Duration(milliseconds: 2500),
  }) : super(key: key);

  @override
  State<ConfettiParticleWidget> createState() => ConfettiParticleWidgetState();
}

class ConfettiParticleWidgetState extends State<ConfettiParticleWidget>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  final List<_ConfettiParticle> _particles = [];
  final Random _random = Random();

  static const List<Color> _vibrantColors = [
    Color(0xFFF59E0B), // Gold
    Color(0xFF10B981), // Emerald
    Color(0xFF06B6D4), // Cyan
    Color(0xFF7C3AED), // Purple
    Color(0xFFEC4899), // Pink
    Color(0xFFEF4444), // Coral
    Color(0xFF3B82F6), // Blue
  ];

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: widget.duration,
    )..addListener(() {
        _updateParticles();
      });

    if (widget.autoStart) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        play();
      });
    }
  }

  void play() {
    _initParticles();
    _controller.forward(from: 0.0);
  }

  void _initParticles() {
    _particles.clear();
    const int count = 75;
    for (int i = 0; i < count; i++) {
      final angle = _random.nextDouble() * 2 * pi;
      final speed = 3.0 + _random.nextDouble() * 7.0;
      _particles.add(
        _ConfettiParticle(
          x: 0,
          y: 0,
          vx: cos(angle) * speed,
          vy: sin(angle) * speed - 5.0, // Initial upward burst
          rotation: _random.nextDouble() * 2 * pi,
          rotationSpeed: (_random.nextDouble() - 0.5) * 0.3,
          size: 6.0 + _random.nextDouble() * 8.0,
          color: _vibrantColors[_random.nextInt(_vibrantColors.length)],
          shape: ConfettiShape.values[_random.nextInt(ConfettiShape.values.length)],
        ),
      );
    }
  }

  void _updateParticles() {
    const double gravity = 0.25;
    const double drag = 0.96;

    for (final p in _particles) {
      p.x += p.vx;
      p.y += p.vy;
      p.vx *= drag;
      p.vy = (p.vy + gravity) * drag;
      p.rotation += p.rotationSpeed;
    }
    setState(() {});
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      clipBehavior: Clip.none,
      alignment: Alignment.center,
      children: [
        widget.child,
        if (_controller.isAnimating)
          Positioned.fill(
            child: IgnorePointer(
              child: CustomPaint(
                painter: _ConfettiPainter(
                  particles: _particles,
                  progress: _controller.value,
                ),
              ),
            ),
          ),
      ],
    );
  }
}

class _ConfettiPainter extends CustomPainter {
  final List<_ConfettiParticle> particles;
  final double progress;

  _ConfettiPainter({required this.particles, required this.progress});

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final opacity = (1.0 - progress).clamp(0.0, 1.0);

    for (final p in particles) {
      final paint = Paint()
        ..color = p.color.withOpacity(opacity)
        ..style = PaintingStyle.fill;

      canvas.save();
      canvas.translate(center.dx + p.x, center.dy + p.y);
      canvas.rotate(p.rotation);

      switch (p.shape) {
        case ConfettiShape.rectangle:
          canvas.drawRect(
            Rect.fromCenter(center: Offset.zero, width: p.size, height: p.size * 0.5),
            paint,
          );
          break;
        case ConfettiShape.circle:
          canvas.drawCircle(Offset.zero, p.size * 0.4, paint);
          break;
        case ConfettiShape.diamond:
          final path = Path()
            ..moveTo(0, -p.size * 0.6)
            ..lineTo(p.size * 0.4, 0)
            ..lineTo(0, p.size * 0.6)
            ..lineTo(-p.size * 0.4, 0)
            ..close();
          canvas.drawPath(path, paint);
          break;
      }
      canvas.restore();
    }
  }

  @override
  bool shouldRepaint(covariant _ConfettiPainter oldDelegate) => true;
}
