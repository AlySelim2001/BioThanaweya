import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../data/datasources/architecture_local_data_source.dart';
import '../data/repositories/architecture_hub_repository_impl.dart';
import '../domain/entities/architecture_metrics.dart';
import '../domain/repositories/architecture_hub_repository.dart';
import '../domain/usecases/load_architecture_metrics.dart';
import '../domain/usecases/run_local_database_benchmark.dart';
import '../../../core/database/local_database_service.dart';

