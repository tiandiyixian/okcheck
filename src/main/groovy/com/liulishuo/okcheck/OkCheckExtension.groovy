/*
 * Copyright (c) 2018 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.okcheck

import com.android.annotations.NonNull
import com.android.build.gradle.internal.dsl.TestOptions
import com.liulishuo.okcheck.util.DestinationUtil
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.FindBugsExtension
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.util.ConfigureUtil

class OkCheckExtension {
    @NonNull
    private final CheckStyleOptions checkstyle

    @NonNull
    private final FindBugsOptions findbugs

    @NonNull
    private final PmdOptions pmd

    @NonNull
    private final KtLintOptions ktlint

    @NonNull
    private final LintOptions lint

    @NonNull
    private final UnitTestOptions unitTest

    @NonNull
    private final CoverageReportOptions coverageReport

    OkCheckExtension(Project project) {
        File destination = project.rootProject.getBuildDir()

        checkstyle = new CheckStyleOptions(project)
        checkstyle.setDestination(destination)

        findbugs = new FindBugsOptions(project)
        findbugs.setDestination(destination)

        pmd = new PmdOptions(project)
        pmd.setDestination(destination)

        ktlint = new KtLintOptions(project)
        ktlint.setDestination(destination)

        lint = new LintOptions(project)
        lint.setDestination(destination)

        unitTest = new UnitTestOptions(project)
        unitTest.setDestination(destination)

        coverageReport = new CoverageReportOptions(project)
        coverageReport.setDestination(destination)
    }

    void setExclude(String[] exclude) {
        this.findbugs.exclude = exclude
        this.checkstyle.exclude = exclude
        this.pmd.exclude = exclude
        this.ktlint.exclude = exclude
        this.lint.exclude = exclude
    }

    void setDestination(File destination) {
        this.findbugs.setDestination(destination)
        this.checkstyle.setDestination(destination)
        this.pmd.setDestination(destination)
        this.ktlint.setDestination(destination)
        this.lint.setDestination(destination)
        this.unitTest.setDestination(destination)
        this.coverageReport.setDestination(destination)
    }

    void checkstyle(Closure closure) {
        ConfigureUtil.configure(closure, checkstyle)
    }

    void findbugs(Closure closure) {
        ConfigureUtil.configure(closure, findbugs)
    }

    void pmd(Closure closure) {
        ConfigureUtil.configure(closure, pmd)
    }

    void ktlint(Closure closure) {
        ConfigureUtil.configure(closure, ktlint)
    }

    void lint(Closure closure) {
        ConfigureUtil.configure(closure, lint)
    }

    void unittest(Closure closure) {
        ConfigureUtil.configure(closure, unitTest)
    }

    void coverageReport(Closure closure) {
        ConfigureUtil.configure(closure, coverageReport)
    }

    @NonNull
    CheckStyleOptions getCheckStyle() {
        return checkstyle
    }

    @NonNull
    FindBugsOptions getFindbugs() {
        return findbugs
    }

    @NonNull
    PmdOptions getPmd() {
        return pmd
    }

    @NonNull
    KtLintOptions getKtlint() {
        return ktlint
    }

    @NonNull
    LintOptions getLint() {
        return lint
    }

    @NonNull
    UnitTestOptions getUnitTest() {
        return unitTest
    }

    @NonNull
    CoverageReportOptions getCoverageReport() {
        return coverageReport
    }

    static class CheckStyleOptions extends CheckstyleExtension {

        boolean enabled = true

        @NonNull
        private final CommonHelper common


        CheckStyleOptions(Project project) {
            super(project)
            common = new CommonHelper(project, "checkstyle")
        }

        void setDestination(File destination) {
            common.setDestination(destination)
        }

        @NonNull
        File getHtmlFile() {
            return common.htmlFile
        }

        void setExclude(String... exclude) {
            common.exclude = exclude
        }

        @NonNull
        String[] getExclude() {
            return common.exclude
        }
    }

    static class UnitTestOptions extends TestOptions.UnitTestOptions {
        boolean enabled = true

        @NonNull
        private File reportDir

        @NonNull
        private final Project project

        UnitTestOptions(Project project) {
            this.project = project
        }

        void setDestination(File destination) {
            reportDir = DestinationUtil.getDirDest(project, destination, "tests")
        }

        File getReportDir() {
            return reportDir
        }
    }

    static class LintOptions extends com.android.build.gradle.internal.dsl.LintOptions {
        boolean enabled = true

        @NonNull
        private final CommonHelper common


        LintOptions(Project project) {
            common = new CommonHelper(project, "lint")
            this.htmlReport = true
        }

        void setDestination(File destination) {
            common.setDestination(destination)
            this.htmlOutput = common.htmlFile
        }

        void setExclude(String... exclude) {
            common.exclude = exclude
        }

        @NonNull
        String[] getExclude() {
            return common.exclude
        }
    }

    static class KtLintOptions {
        boolean enabled = true

        @NonNull
        String version = "0.22.0"

        @NonNull
        private final CommonHelper common


        KtLintOptions(Project project) {
            common = new CommonHelper(project, "ktlint")
        }

        void setDestination(File destination) {
            common.setDestination(destination)
        }

        @NonNull
        File getXmlFile() {
            return common.xmlFile
        }

        void setExclude(String... exclude) {
            common.exclude = exclude
        }

        @NonNull
        String[] getExclude() {
            return common.exclude
        }
    }

    static class CoverageReportOptions {

        Report xml
        Report html
        Report csv

        @NonNull
        private File xmlFile

        @NonNull
        private File htmlFile

        @NonNull
        private File csvFile

        private final Project project


        CoverageReportOptions(Project project) {
            this.project = project
            xml = new Report()
            html = new Report()
            csv = new Report()
        }

        void setDestination(File destination) {
            File dir = DestinationUtil.getDirDest(project, destination, "coverage")
            this.htmlFile = dir
            this.xmlFile = new File(dir, "report.xml")
            this.csvFile = new File(dir, "report.csv")
        }

        @NonNull
        File getHtmlFile() {
            return this.htmlFile
        }

        @NonNull
        File getXmlFile() {
            return this.xmlFile
        }

        @NonNull
        File getCsvFile() {
            return this.csvFile
        }

        boolean isEnabled() {
            return xml.enabled || html.enabled || csv.enabled
        }
    }

    static class Report {
        boolean enabled = false
    }

    static class FindBugsOptions extends FindBugsExtension {

        boolean enabled = true

        boolean reportXml = false
        boolean reportHtml = true

        @NonNull
        private final CommonHelper common


        FindBugsOptions(Project project) {
            super(project)
            common = new CommonHelper(project, "findbugs")
        }

        void setDestination(File destination) {
            common.setDestination(destination)
        }

        @NonNull
        File getHtmlFile() {
            return common.htmlFile
        }

        @NonNull
        File getXmlFile() {
            return common.xmlFile
        }

        void setExclude(String... exclude) {
            common.exclude = exclude
        }

        @NonNull
        String[] getExclude() {
            return common.exclude
        }
    }

    static class PmdOptions extends PmdExtension {

        boolean enabled = true

        @NonNull
        private final CommonHelper common


        PmdOptions(Project project) {
            super(project)
            common = new CommonHelper(project, "pmd")
        }

        void setDestination(File destination) {
            common.setDestination(destination)
        }

        @NonNull
        File getHtmlFile() {
            return common.htmlFile
        }

        void setExclude(String... exclude) {
            common.exclude = exclude
        }

        @NonNull
        String[] getExclude() {
            return common.exclude
        }
    }

    static class CommonHelper {

        @NonNull
        private final String name

        @NonNull
        private final Project project
        @NonNull
        File htmlFile
        @NonNull
        File xmlFile
        @NonNull
        String[] exclude = []

        CommonHelper(Project project, String name) {
            this.name = name
            this.project = project
        }

        void setDestination(File destination) {
            htmlFile = DestinationUtil.getHtmlDest(project, destination, name)
            xmlFile = DestinationUtil.getXmlDest(project, destination, name)
        }
    }
}