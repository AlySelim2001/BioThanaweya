import 'package:flutter/material.dart';

/// ويدجت مخطط المسار الصغير (Sparkline Chart) عالي الدقة لعرض منحنى تطور استيعاب الطالب عبر الزمن
class SparklineChart extends StatelessWidget {
  final List<double> data;
  final Color lineColor;
  final bool showGradientFill;
  final double strokeWidth;
  final bool showEndDot;
  final double width;
  final double height;

  const SparklineChart({
    Key? key,
    required this.data,
    this.lineColor = const Color(0xFF0D9488),
    this.showGradientFill = true,
    this.strokeWidth = 2.0,
    this.showEndDot = true,
    this.width = 75,
    this.height = 28,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    if (data.isEmpty) {
      return SizedBox(width: width, height: height);
    }

    return SizedBox(
      width: width,
      height: height,
      child: CustomPaint(
        painter: _SparklinePainter(
          data: data,
          lineColor: lineColor,
          showGradientFill: showGradientFill,
          strokeWidth: strokeWidth,
          showEndDot: showEndDot,
        ),
      ),
    );
  }
}

class _SparklinePainter extends CustomPainter {
  final List<double> data;
  final Color lineColor;
  final bool showGradientFill;
  final double strokeWidth;
  final bool showEndDot;

  _SparklinePainter({
    required this.data,
    required this.lineColor,
    required this.showGradientFill,
    required this.strokeWidth,
    required this.showEndDot,
  });

  @override
  void paint(Canvas canvas, Size size) {
    if (data.length < 2) return;

    final double minVal = 0.0;
    final double maxVal = 1.0;
    final double range = maxVal - minVal;

    final double stepX = size.width / (data.length - 1);
    final List<Offset> points = [];

    for (int i = 0; i < data.length; i++) {
      final double normalizedY = ((data[i] - minVal) / (range == 0 ? 1 : range)).clamp(0.0, 1.0);
      final double x = i * stepX;
      // Invert Y so 1.0 is at top (0.0 y in Canvas) with 2px padding
      final double y = (size.height - 4) * (1.0 - normalizedY) + 2;
      points.add(Offset(x, y));
    }

    // Build smooth cubic bezier path
    final Path path = Path();
    path.moveTo(points.first.dx, points.first.dy);

    for (int i = 0; i < points.length - 1; i++) {
      final p0 = points[i];
      final p1 = points[i + 1];
      final controlPointX = (p0.dx + p1.dx) / 2;
      path.cubicTo(
        controlPointX,
        p0.dy,
        controlPointX,
        p1.dy,
        p1.dx,
        p1.dy,
      );
    }

    // 1. Draw gradient fill underneath the curve if enabled
    if (showGradientFill) {
      final Path fillPath = Path.from(path)
        ..lineTo(points.last.dx, size.height)
        ..lineTo(points.first.dx, size.height)
        ..close();

      final Paint fillPaint = Paint()
        ..shader = LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            lineColor.withValues(alpha: 0.25),
            lineColor.withValues(alpha: 0.0),
          ],
        ).createShader(Rect.fromLTWH(0, 0, size.width, size.height))
        ..style = PaintingStyle.fill;

      canvas.drawPath(fillPath, fillPaint);
    }

    // 2. Draw line stroke
    final Paint strokePaint = Paint()
      ..color = lineColor
      ..strokeWidth = strokeWidth
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.round
      ..strokeJoin = StrokeJoin.round;

    canvas.drawPath(path, strokePaint);

    // 3. Draw glowing dot at the end representing current progress
    if (showEndDot && points.isNotEmpty) {
      final Offset lastPoint = points.last;

      // Glow halo
      final Paint glowPaint = Paint()
        ..color = lineColor.withValues(alpha: 0.3)
        ..style = PaintingStyle.fill;
      canvas.drawCircle(lastPoint, strokeWidth * 2.5, glowPaint);

      // Solid core
      final Paint dotPaint = Paint()
        ..color = lineColor
        ..style = PaintingStyle.fill;
      canvas.drawCircle(lastPoint, strokeWidth * 1.5, dotPaint);
    }
  }

  @override
  bool shouldRepaint(covariant _SparklinePainter oldDelegate) =>
      oldDelegate.data != data || oldDelegate.lineColor != lineColor;
}
