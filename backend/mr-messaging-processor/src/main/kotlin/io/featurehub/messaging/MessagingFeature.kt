package io.featurehub.messaging

import io.featurehub.events.kinesis.KinesisEventFeature
import io.featurehub.events.pubsub.GoogleEventFeature
import io.featurehub.messaging.converter.FeatureMessagingConverter
import io.featurehub.messaging.converter.FeatureMessagingConverterImpl
import io.featurehub.messaging.publisher.kinesis.KinesisFeatureMessagingPublisher
import io.featurehub.messaging.publisher.nats.NatsFeatureMessagingPublisher
import io.featurehub.messaging.publisher.pubsub.PubsubFeatureMessagingPublisher
import io.featurehub.messaging.service.FeatureMessagingCloudEventPublisher
import io.featurehub.messaging.service.FeatureMessagingCloudEventPublisherImpl
import io.featurehub.publish.NATSFeature
import jakarta.inject.Singleton
import jakarta.ws.rs.core.Feature
import jakarta.ws.rs.core.FeatureContext
import org.glassfish.hk2.api.Immediate
import org.glassfish.jersey.internal.inject.AbstractBinder

class MessagingFeature: Feature {
  override fun configure(ctx: FeatureContext): Boolean {
    ctx.register(object: AbstractBinder() {

      override fun configure() {
        bind(FeatureMessagingCloudEventPublisherImpl::class.java).to(FeatureMessagingCloudEventPublisher::class.java).`in`(Singleton::class.java)
        bind(FeatureMessagingConverterImpl::class.java).to(FeatureMessagingConverter::class.java).`in`(Singleton::class.java)

        if (NATSFeature.isNatsConfigured()) {
          bind(NatsFeatureMessagingPublisher::class.java).to(NatsFeatureMessagingPublisher::class.java).`in`(Immediate::class.java)
        }

        if (KinesisEventFeature.isEnabled()) {
          bind(KinesisFeatureMessagingPublisher::class.java).to(KinesisFeatureMessagingPublisher::class.java).`in`(Immediate::class.java)
        }

        if (GoogleEventFeature.isEnabled()) {
          bind(PubsubFeatureMessagingPublisher::class.java).to(PubsubFeatureMessagingPublisher::class.java).`in`(Immediate::class.java)
        }
      }

    })
    return true
  }
}
