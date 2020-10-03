package com.rthoughts.genie.test

import cucumber.api.CucumberOptions

@CucumberOptions(features = ["features"], glue = ["com.rthoughts.genie.steps"])
class CucumberTestCase {}