package io.featurehub.db.services

import io.featurehub.db.model.*

interface ArchiveStrategy {
  fun archivePortfolio(portfolio: DbPortfolio)
  fun archiveApplication(application: DbApplication)
  fun archiveEnvironment(environment: DbEnvironment)
  fun archiveOrganization(organization: DbOrganization)
  fun archiveServiceAccount(serviceAccount: DbServiceAccount)
  fun archiveGroup(group: DbGroup)
  fun archiveApplicationFeature(feature: DbApplicationFeature)
  fun archivePerson(person: DbPerson)
}
