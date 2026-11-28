import 'package:flutter/material.dart';
import '../../domain/entities/muscle_energetics_result.dart';

/// Dynamic CustomPainter widget illustrating the muscle sarcomere bands and zones in real-time.
class SarcomereDiagramView extends StatelessWidget {
  final SarcomereCalculationResult result;

  const SarcomereDiagramView({
    Key? key,
    required this.result,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: const Color(0xFF0F172A),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: const Color(0xFF334155)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Row(
            children: [
              const Icon(Icons.linear_scale_rounded,
                  color: Color(0xFFF59E0B), size: 20),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  'المخطط التوضيحي للقطعة العضلية (${result.isFullyContracted ? "حالة انقباض تام" : "حالة انبساط"})',
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                  ),
                ),
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: result.isFullyContracted
                      ? const Color(0x33EF4444)
                      : const Color(0x3310B981),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  result.isFullyContracted ? 'H = 0 (تلاشي)' : 'H = ${result.hZonesRelaxedCount}',
                  style: TextStyle(
                    color: result.isFullyContracted
                        ? const Color(0xFFF87171)
                        : const Color(0xFF34D399),
                    fontWeight: FontWeight.bold,
                    fontSize: 11,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          SizedBox(
            height: 90,
            child: CustomPaint(
              painter: _SarcomerePainter(
                sarcomereCount: result.numberOfSarcomeres.clamp(1, 4),
                isContracted: result.isFullyContracted,
              ),
            ),
          ),
          const SizedBox(height: 12),
          // Legend row
          Wrap(
            alignment: WrapAlignment.center,
            spacing: 12,
            runSpacing: 6,
            children: [
              _legendItem('خط Z (متعرج)', const Color(0xFFEF4444)),
              _legendItem('أكتين رفيع (I)', const Color(0xFF38BDF8)),
              _legendItem('ميوسين سميك (A)', const Color(0xFFF59E0B)),
              _legendItem('منطقة H (شبه مضيئة)', const Color(0xFFA78BFA)),
            ],
          ),
        ],
      ),
    );
  }

  Widget _legendItem(String label, Color color) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 10,
          height: 10,
          decoration: BoxDecoration(
            color: color,
            shape: BoxShape.circle,
          ),
        ),
        const SizedBox(width: 4),
        Text(
          label,
          style: const TextStyle(color: Color(0xFF94A3B8), fontSize: 10),
        ),
      ],
    );
  }
}

class _SarcomerePainter extends CustomPainter {
  final int sarcomereCount;
  final bool isContracted;

  _SarcomerePainter({
    required this.sarcomereCount,
    required this.isContracted,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final sarcomereWidth = size.width / sarcomereCount;
    final zPaint = Paint()
      ..color = const Color(0xFFEF4444)
      ..strokeWidth = 3.0
      ..style = PaintingStyle.stroke;

    final myosinPaint = Paint()
      ..color = const Color(0xFFF59E0B)
      ..strokeWidth = 5.0
      ..strokeCap = StrokeCap.round;

    final actinPaint = Paint()
      ..color = const Color(0xFF38BDF8)
      ..strokeWidth = 2.5
      ..strokeCap = StrokeCap.round;

    final hZonePaint = Paint()
      ..color = isContracted
          ? Colors.transparent
          : const Color(0x33A78BFA)
      ..style = PaintingStyle.fill;

    for (int i = 0; i < sarcomereCount; i++) {
      final startX = i * sarcomereWidth;
      final endX = startX + sarcomereWidth;
      final centerX = (startX + endX) / 2;

      // Draw Z-lines (start and end)
      _drawZLine(canvas, startX, size.height, zPaint);
      if (i == sarcomereCount - 1) {
        _drawZLine(canvas, endX, size.height, zPaint);
      }

      // Myosin width is constant in sarcomere center
      final myosinHalfWidth = sarcomereWidth * 0.28;
      final myosinLeft = centerX - myosinHalfWidth;
      final myosinRight = centerX + myosinHalfWidth;

      // H-zone background (between actin tips in center)
      final actinOverlapRatio = isContracted ? 0.95 : 0.45;
      final actinReach = (sarcomereWidth / 2) * actinOverlapRatio;
      final leftActinTip = startX + actinReach;
      final rightActinTip = endX - actinReach;

      if (!isContracted && rightActinTip > leftActinTip) {
        final hRect = Rect.fromLTRB(leftActinTip, 10, rightActinTip, size.height - 10);
        canvas.drawRRect(RRect.fromRectAndRadius(hRect, const Radius.circular(4)), hZonePaint);
      }

      // Draw Myosin thick filaments (3 parallel bars)
      for (double yOffset in [-12.0, 0.0, 12.0]) {
        final y = (size.height / 2) + yOffset;
        canvas.drawLine(Offset(myosinLeft, y), Offset(myosinRight, y), myosinPaint);
      }

      // Draw Actin thin filaments extending from Z lines
      for (double yOffset in [-18.0, -6.0, 6.0, 18.0]) {
        final y = (size.height / 2) + yOffset;
        // From left Z-line
        canvas.drawLine(Offset(startX, y), Offset(startX + actinReach, y), actinPaint);
        // From right Z-line
        canvas.drawLine(Offset(endX, y), Offset(endX - actinReach, y), actinPaint);
      }
    }
  }

  void _drawZLine(Canvas canvas, double x, double height, Paint paint) {
    final path = Path();
    path.moveTo(x, 4);
    const steps = 6;
    final stepH = (height - 8) / steps;
    for (int i = 0; i < steps; i++) {
      final zigX = i.isEven ? x - 4 : x + 4;
      path.lineTo(zigX, 4 + (i + 0.5) * stepH);
      path.lineTo(x, 4 + (i + 1) * stepH);
    }
    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(covariant _SarcomerePainter oldDelegate) {
    return oldDelegate.sarcomereCount != sarcomereCount ||
        oldDelegate.isContracted != isContracted;
  }
}
