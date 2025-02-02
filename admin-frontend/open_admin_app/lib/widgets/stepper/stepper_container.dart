import 'package:bloc_provider/bloc_provider.dart';
import 'package:flutter/material.dart';

import 'package:open_admin_app/api/client_api.dart';
import 'package:open_admin_app/common/stream_valley.dart';
import 'package:open_admin_app/widgets/stepper/progress_stepper_bloc.dart';
import 'package:open_admin_app/widgets/stepper/progress_stepper_widget.dart';

class StepperContainer extends StatelessWidget {
  final ManagementRepositoryClientBloc mrBloc;

  const StepperContainer({
    Key? key,
    required this.mrBloc,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<bool>(
        initialData: false,
        stream: mrBloc.stepperOpened,
        builder: (context, snapshot) {
          if (snapshot.hasData && snapshot.data == true) {
            return BlocProvider(
                creator: (_context, _bag) => StepperBloc(mrBloc),
                child: const FHSetupProgressStepper());
          } else {
            return const SizedBox.shrink();
          }
        });
  }
}

class StepperRocketButton extends StatelessWidget {
  final ManagementRepositoryClientBloc mrBloc;

  const StepperRocketButton({
    Key? key,
    required this.mrBloc,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<ReleasedPortfolio?>(
        stream: mrBloc.streamValley.currentPortfolioStream,
        builder: (context, snapshot) {
          if (snapshot.data != null &&
              (snapshot.data!.currentPortfolioOrSuperAdmin == true)) {
            return IconButton(
              tooltip: 'Open quick setup',
              icon: Icon(
                Icons.rocket,
                color: Theme.of(context).colorScheme.primary,
              ),
              onPressed: () => mrBloc.stepperOpened = true,
            );
          } else {
            return const SizedBox.shrink();
          }
        });
  }
}
