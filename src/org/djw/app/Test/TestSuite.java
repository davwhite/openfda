package org.djw.app.Test;

import java.util.ArrayList;

public class TestSuite {

	public static void main(String[] args) {
		boolean RunSilent = true;
		boolean runTest = false;
		String TestResult = "FAIL";
		int TotalTested = 0;
		int TotalPassed = 0;
		int TotalFailed = 0;

		if (args.length <= 0){
			System.out.println("usage: TestSuite [configFile] [-options] ([testModule])");
			System.out.println("options: -s  silent. only returns overall result");
			System.out.println("options: -v  verbose. returns human readable output.");
			runTest = false;
		} else {
			runTest = true;
		}
		try{
			String opt = args[1];
			if (opt.equals("-s")){
				RunSilent = true;
			}
			if (opt.equals("-v")){
				RunSilent = false;
			}
		} catch (Exception x){
		}

		if (runTest){
			if (!RunSilent) System.out.println("================== ApplicationRx Test Suite ==================");
			try{
				String ConfigFile = args[0]; 
				Config config = new Config();
				String ServerKey = config.getConfigValue(ConfigFile, "server_key");
				String ServiceURL = config.getConfigValue(ConfigFile, "service_url");
				ArrayList<String> FailedModules = new ArrayList<String>();
				
				//Test database lookup service
				String TestLookupPartial = config.getConfigValue(ConfigFile, "test_lookup_partial");
				String TestLookupMatch = config.getConfigValue(ConfigFile, "test_lookup_match");
				TestLookup testLookup = new TestLookup();
				String TestLookupURL = ServiceURL + "/fda/" + ServerKey + "/lookup/drugs/" + TestLookupPartial;
				boolean testDrugPartial = testLookup.testDrugPartial(TestLookupURL, TestLookupMatch);
				TotalTested++;
				if (testDrugPartial){
					TotalPassed++;
				} else {
					TotalFailed++;
					FailedModules.add("testLookup");
				}

				//Test reaction search service
				String TestReactionSearch = config.getConfigValue(ConfigFile, "test_reaction_search");
				String TestReactionMatch = config.getConfigValue(ConfigFile, "test_reaction_match");
				TestSearchReaction testSearchReaction = new TestSearchReaction();
				String TestReactionURL = ServiceURL + "/fda/" + ServerKey + "/search/reaction/" + TestReactionSearch;
				boolean ReactionSearch = testSearchReaction.testReaction(TestReactionURL, TestReactionMatch);
				TotalTested++;
				if (ReactionSearch){
					TotalPassed++;
				} else {
					TotalFailed++;
					FailedModules.add("testSearchReaction");
				}


				//Test drug search service
				String TestDrugSearch = config.getConfigValue(ConfigFile, "test_drug_search");
				String TestDrugMatch = config.getConfigValue(ConfigFile, "test_drug_match");
				TestSearchDrug testSearchDrug = new TestSearchDrug();
				String TestDrugURL = ServiceURL + "/fda/" + ServerKey + "/search/drug/" + TestDrugSearch;
				boolean DrugSearch = testSearchDrug.testDrug(TestDrugURL, TestDrugMatch);
				TotalTested++;
				if (DrugSearch){
					TotalPassed++;
				} else {
					TotalFailed++;
					FailedModules.add("testSearchDrug");
				}

				//Test drug info service
				String TestDrugInfoSearch = config.getConfigValue(ConfigFile, "test_druginfo_search");
				String TestDrugInfoMatch = config.getConfigValue(ConfigFile, "test_druginfo_match");
				TestDrugInfo testDrugInfo = new TestDrugInfo();
				String TestDrugInfoURL = ServiceURL + "/fda/" + ServerKey + "/druginfo/" + TestDrugInfoSearch;
				boolean DrugInfo = testDrugInfo.testDrugInfo(TestDrugInfoURL, TestDrugInfoMatch);
				TotalTested++;
				if (DrugInfo){
					TotalPassed++;
				} else {
					TotalFailed++;
					FailedModules.add("testDrugInfo");
				}

				//Test chart service
				String TestChartSearch = config.getConfigValue(ConfigFile, "test_chart_search");
				TestChart testChart = new TestChart();
				String TestChartURL = ServiceURL + "/fda/" + ServerKey + "/chart/drugs/" + TestChartSearch;
				boolean Chart = testChart.testChart(TestChartURL);
				TotalTested++;
				if (Chart){
					TotalPassed++;
				} else {
					TotalFailed++;
					FailedModules.add("testChart");
				}
				
				
				if (TotalFailed == 0) TestResult = "PASS";
				if (!RunSilent) System.out.println("================== Statistics ==================");
				if (!RunSilent) System.out.println("Modules Tested: " + TotalTested);
				if (!RunSilent) System.out.println("Modules Passed: " + TotalPassed);
				if (!RunSilent) System.out.println("Modules Failed: " + TotalFailed);
				if (!RunSilent && TotalFailed > 0) System.out.println("Failed Modules: " + FailedModules.toString());
			} catch (Exception e){
				System.out.println("TestSuite: an error has occurred: " + e);
				TestResult = "FAIL";
			}
		}
		if (RunSilent) {
			System.out.println(TestResult);
		} else {
			System.out.println("OVERALL RESULT: " + TestResult);
		}
		if (TestResult.equals("PASS")){
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

}
