package io.featurehub.mr.events.service

import io.featurehub.db.api.RolloutStrategyValidator.InvalidStrategyCombination
import io.featurehub.db.listener.FeatureUpdateBySDKApi
import io.featurehub.events.CloudEventReceiverRegistry
import io.featurehub.mr.events.common.listeners.FeatureUpdateListener
import io.featurehub.mr.messaging.StreamedFeatureUpdate
import io.featurehub.mr.model.FeatureValue
import io.featurehub.mr.model.FeatureValueType
import jakarta.inject.Inject
import org.slf4j.LoggerFactory

open class FeatureUpdateListenerImpl @Inject constructor(
  private val featureUpdateBySDKApi: FeatureUpdateBySDKApi,
  cloudEventReceiverRegistry: CloudEventReceiverRegistry
) : FeatureUpdateListener {

  init {
    cloudEventReceiverRegistry.listen(StreamedFeatureUpdate::class.java) { it, ce ->
      processUpdate(it)
    }
  }

  override fun processUpdate(update: StreamedFeatureUpdate) {
    try {
      log.debug("received update {}", update)
      featureUpdateBySDKApi.updateFeatureFromTestSdk(
        update.apiKey, update.environmentId, update.featureKey,  update.updatingValue, update.lock != null
      ) { valueType: FeatureValueType? ->
        val fv = FeatureValue()
          .key(update.featureKey)
          .locked(update.lock != null && update.lock!!)

        if (update.updatingValue) {
          when (valueType) {
            FeatureValueType.BOOLEAN -> fv.valueBoolean(update.valueBoolean)
            FeatureValueType.STRING -> fv.valueString(update.valueString)
            FeatureValueType.NUMBER -> fv.valueNumber = update.valueNumber
            FeatureValueType.JSON -> fv.valueJson(update.valueString)
            else -> {
            }
          }
        }

        fv
      }
    } catch (ignoreEx: InvalidStrategyCombination) {
      // ignore
    } catch (failed: Exception) {
      log.error("Failed to update {}", update, failed)
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(FeatureUpdateListener::class.java)
  }
}
