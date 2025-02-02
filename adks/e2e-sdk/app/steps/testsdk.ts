import { SdkWorld } from '../support/world';
import { Given } from '@cucumber/cucumber';
import { FeatureStateUpdate } from '../apis/edge';
import { expect } from 'chai';

Given(/^I use the Test SDK to update feature (.*) to (locked|unlocked) and (on|off)$/, async function(key: string,
                          locked: string, onoff: string) {
  const world = this as SdkWorld;

  const response = await world.edgeApi.setFeatureState(world.serviceAccountPermission.sdkUrlClientEval.replace("/", "+"),
    key, new FeatureStateUpdate({lock: locked === 'locked', updateValue: true, value: 'on' == onoff}));
  expect(response.status >= 200 && response.status < 300).to.be.true;
});
