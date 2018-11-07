/*
 *
 *   Copyright IBM Corp. 2018
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package odm.ds.kafka.odmj2seclient;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;


/**
 * Class responsible for testing the Main.main() behaviour
 * 
 *
 */
public class SampleTest {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testNoRulesetPath() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String argLine = "-r blahblah";
        Main.main(argLine.split(" "));
    }

    @Test
    public void testNoRuleAppArgumentValue() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String argLine = "-r /foo/bar";
        Main.main(argLine.split(" "));
    }

    @Test
    public void testInvalidRulesetPath() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String argLine = "foo/bar";
        Main.main(argLine.split(" "));
    }

    @Test
    public void testRuleAppArchiveFileNotFound() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String argLine = "-r blablah /foo/bar";
        Main.main(argLine.split(" "));
    }

    @Test
    public void testRulesetNotBundled() throws Exception {
        try {
            exit.expectSystemExitWithStatus(2);
            String argLine = "/foo/bar";
            Main.main(argLine.split(" "));
        } finally {
            String expectedErrorMessage = "It was not possible to retrieve the information about the ruleset /foo/bar.";
            assertTrue("SysErr does not contain the error message '" + expectedErrorMessage + "'",
                    systemErrRule.getLog().contains(expectedErrorMessage));
            systemErrRule.clearLog();
        }
    }

    @Test
    public void testNoRuleAppArchiveArgument() throws Exception {
        String argLine = "/test_deployment/loan_validation_with_score_and_grade";
        Main.main(argLine.split(" "));
    }

    @Test
    public void testNotAnArchive() throws Exception {
        try {
            exit.expectSystemExitWithStatus(2);
            String argLine = "--ruleApp notanarchive /foo/bar";
            Main.main(argLine.split(" "));
        } finally {
            String expectedErrorMessage = "The RuleApp archive is not valid.";
            assertTrue("SysErr does not contain the error message '" + expectedErrorMessage + "'",
                    systemErrRule.getLog().contains(expectedErrorMessage));
            systemErrRule.clearLog();
        }
    }

    @Test
    public void testNotARuleAppArchive() throws Exception {
        try {
            exit.expectSystemExitWithStatus(2);
            String argLine = "--ruleApp notaruleapparchive.zip /foo/bar";
            Main.main(argLine.split(" "));
        } finally {
            String expectedErrorMessage = "The RuleApp archive is not valid.";
            assertTrue("SysErr does not contain the error message '" + expectedErrorMessage + "'",
                    systemErrRule.getLog().contains(expectedErrorMessage));
            systemErrRule.clearLog();
        }
    }
}
