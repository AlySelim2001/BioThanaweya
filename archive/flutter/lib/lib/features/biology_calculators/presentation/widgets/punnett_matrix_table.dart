import 'package:flutter/material.dart';
import '../../domain/entities/genetics_cross_result.dart';

/// Interactive visual widget for displaying Monohybrid (2x2) and Dihybrid (4x4) Punnett Squares.
class PunnettMatrixTable extends StatelessWidget {
  final GeneticsCrossResult result;

  const PunnettMatrixTable({
    Key? key,
    required this.result,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final gametesTop = result.gametesParent1;
    final gametesSide = result.gametesParent2;
    final matrix = result.matrix;

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
              const Icon(Icons.grid_4x4_rounded, color: Color(0xFFA78BFA), size: 20),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  'مربع بانيت التفاعلي (${result.parent1Genotype} × ${result.parent2Genotype})',
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 14,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Table(
              defaultColumnWidth: const FixedColumnWidth(76.0),
              border: TableBorder.all(
                color: const Color(0xFF475569),
                borderRadius: BorderRadius.circular(8),
                width: 1.0,
              ),
              children: [
                // Header row with top gametes
                TableRow(
                  decoration: const BoxDecoration(color: Color(0xFF1E293B)),
                  children: [
                    const Padding(
                      padding: EdgeInsets.all(8.0),
                      child: Center(
                        child: Text(
                          '♂ \\ ♀',
                          style: TextStyle(
                            color: Color(0xFF94A3B8),
                            fontWeight: FontWeight.bold,
                            fontSize: 12,
                          ),
                        ),
                      ),
                    ),
                    for (final g in gametesTop)
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Center(
                          child: Text(
                            g,
                            style: const TextStyle(
                              color: Color(0xFF64FFDA),
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                            ),
                          ),
                        ),
                      ),
                  ],
                ),
                // Data rows
                for (int r = 0; r < gametesSide.length; r++)
                  TableRow(
                    children: [
                      // Side gamete header
                      Container(
                        color: const Color(0xFF1E293B),
                        padding: const EdgeInsets.all(8.0),
                        child: Center(
                          child: Text(
                            gametesSide[r],
                            style: const TextStyle(
                              color: Color(0xFF64FFDA),
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                            ),
                          ),
                        ),
                      ),
                      // Matrix cells
                      for (int c = 0; c < matrix[r].length; c++)
                        _buildCell(matrix[r][c]),
                    ],
                  ),
              ],
            ),
          ),
          const SizedBox(height: 12),
          // Ratio badge summary
          Container(
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              color: const Color(0xFF1E293B),
              borderRadius: BorderRadius.circular(10),
            ),
            child: Row(
              children: [
                const Icon(Icons.pie_chart_outline_rounded,
                    color: Color(0xFF38BDF8), size: 18),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    result.ratioSummary,
                    style: const TextStyle(
                      color: Color(0xFFE2E8F0),
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCell(PunnettCell cell) {
    final isLethal = cell.isLethal;
    return Container(
      color: isLethal
          ? const Color(0x33EF4444)
          : const Color(0xFF0F172A),
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            cell.genotype,
            style: TextStyle(
              color: isLethal ? const Color(0xFFF87171) : Colors.white,
              fontWeight: FontWeight.bold,
              fontSize: 14,
              letterSpacing: 1.1,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            cell.phenotype,
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              color: isLethal
                  ? const Color(0xFFFCA5A5)
                  : const Color(0xFF94A3B8),
              fontSize: 9,
            ),
          ),
        ],
      ),
    );
  }
}
