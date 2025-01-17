/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 * Copyright 2019-2021 the original author or authors.
 */

package org.quickperf.sql.config.library;

import org.quickperf.config.library.AnnotationConfig;
import org.quickperf.sql.PersistenceSqlRecorder;
import org.quickperf.sql.analyze.AnalyzeSqlVerifier;
import org.quickperf.sql.annotation.*;
import org.quickperf.sql.batch.SqlStatementBatchRecorder;
import org.quickperf.sql.batch.SqlStatementBatchVerifier;
import org.quickperf.sql.bindparams.AllParametersAreBoundExtractor;
import org.quickperf.sql.bindparams.DisableQueriesWithoutBindParametersVerifier;
import org.quickperf.sql.connection.ConnectionLeakListener;
import org.quickperf.sql.connection.ConnectionLeakVerifier;
import org.quickperf.sql.connection.TestConnectionProfiler;
import org.quickperf.sql.connection.ProfilingParamsExtractor;
import org.quickperf.sql.delete.DeleteCountMeasureExtractor;
import org.quickperf.sql.delete.MaxOfDeletesPerfIssueVerifier;
import org.quickperf.sql.delete.NumberOfSqlDeletePerfIssueVerifier;
import org.quickperf.sql.display.DisplaySqlOfTestMethodBodyRecorder;
import org.quickperf.sql.display.DisplaySqlRecorder;
import org.quickperf.sql.execution.*;
import org.quickperf.sql.insert.InsertCountMeasureExtractor;
import org.quickperf.sql.insert.InsertNumberPerfIssueVerifier;
import org.quickperf.sql.insert.MaxOfInsertsPerfIssueVerifier;
import org.quickperf.sql.like.ContainsLikeWithLeadingWildcardExtractor;
import org.quickperf.sql.like.HasLikeWithLeadingWildcardVerifier;
import org.quickperf.sql.select.HasExactlySameSelectVerifier;
import org.quickperf.sql.select.HasSameSelectTypesWithDiffParamValuesVerifier;
import org.quickperf.sql.select.MaxOfSelectsPerfIssueVerifier;
import org.quickperf.sql.select.SelectNumberPerfIssueVerifier;
import org.quickperf.sql.select.analysis.SelectAnalysisExtractor;
import org.quickperf.sql.select.columns.MaxSelectedColumnsPerMeasureExtractor;
import org.quickperf.sql.select.columns.MaxSelectedColumnsPerfIssueVerifier;
import org.quickperf.sql.select.columns.SelectedColumnNumberPerfIssueVerifier;
import org.quickperf.sql.select.columns.SelectedColumnNumberPerfMeasureExtractor;
import org.quickperf.sql.statement.NoStatementExtractor;
import org.quickperf.sql.statement.NoStatementVerifier;
import org.quickperf.sql.time.SqlQueryExecutionTimeExtractor;
import org.quickperf.sql.time.SqlQueryMaxExecutionTimeVerifier;
import org.quickperf.sql.update.MaxOfUpdatesPerfIssueVerifier;
import org.quickperf.sql.update.UpdateCountMeasureExtractor;
import org.quickperf.sql.update.UpdateNumberPerfIssueVerifier;
import org.quickperf.sql.update.columns.MaxUpdatedColumnsPerMeasureExtractor;
import org.quickperf.sql.update.columns.MaxUpdatedColumnsPerfIssueVerifier;
import org.quickperf.sql.update.columns.UpdatedColumnsMeasureExtractor;
import org.quickperf.sql.update.columns.UpdatedColumnsPerfIssueVerifier;

class SqlAnnotationsConfigs {

    private SqlAnnotationsConfigs() { }

	static final AnnotationConfig JDBC_QUERY_EXECUTION = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(SqlAnalysisExtractor.INSTANCE)
			.perfIssueVerifier(JdbcQueryExecutionVerifier.INSTANCE)
			.build(ExpectJdbcQueryExecution.class);

	static final AnnotationConfig MAX_JDBC_QUERY_EXECUTION = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(SqlAnalysisExtractor.INSTANCE)
			.perfIssueVerifier(MaxJdbcQueryExecutionVerifier.INSTANCE)
			.build(ExpectMaxJdbcQueryExecution.class);

	static final AnnotationConfig DISABLE_SAME_SQL_SELECTS = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(SelectAnalysisExtractor.INSTANCE)
			.perfIssueVerifier(HasExactlySameSelectVerifier.INSTANCE)
			.build(DisableSameSelects.class);

	static final AnnotationConfig ENABLE_SAME_SQL_SELECTS = new AnnotationConfig.Builder()
			.cancelBehaviorOf(DisableSameSelects.class)
			.build(EnableSameSelects.class);

    static final AnnotationConfig NUMBER_OF_SQL_SELECT = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(SelectAnalysisExtractor.INSTANCE)
            .perfIssueVerifier(SelectNumberPerfIssueVerifier.INSTANCE)
            .build(ExpectSelect.class);

    static final AnnotationConfig MAX_SQL_SELECT = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(SelectAnalysisExtractor.INSTANCE)
            .perfIssueVerifier(MaxOfSelectsPerfIssueVerifier.INSTANCE)
            .build(ExpectMaxSelect.class);

    static final AnnotationConfig MAX_SQL_INSERT = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(InsertCountMeasureExtractor.INSTANCE)
            .perfIssueVerifier(MaxOfInsertsPerfIssueVerifier.INSTANCE)
            .build(ExpectMaxInsert.class);

    static final AnnotationConfig DISPLAY_ALL_SQL = new AnnotationConfig.Builder()
            .perfRecorderClass(DisplaySqlRecorder.class)
            .build(DisplaySql.class);

    static final AnnotationConfig DISPLAY_SQL_OF_TEST_METHOD_BODY = new AnnotationConfig.Builder()
            .perfRecorderClass(DisplaySqlOfTestMethodBodyRecorder.class)
            .build(DisplaySqlOfTestMethodBody.class);

	static final AnnotationConfig DISABLE_STATEMENTS = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(NoStatementExtractor.INSTANCE)
			.perfIssueVerifier(NoStatementVerifier.INSTANCE)
			.build(DisableStatements.class);

	static final AnnotationConfig ENABLE_STATEMENTS = new AnnotationConfig.Builder()
			.cancelBehaviorOf(DisableStatements.class)
			.build(EnableStatements.class);

	static final AnnotationConfig DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAMS = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(SelectAnalysisExtractor.INSTANCE)
			.perfIssueVerifier(HasSameSelectTypesWithDiffParamValuesVerifier.INSTANCE)
			.build(DisableSameSelectTypesWithDifferentParamValues.class);

	static final AnnotationConfig ENABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAMS = new AnnotationConfig.Builder()
			.cancelBehaviorOf(DisableSameSelectTypesWithDifferentParamValues.class)
			.build(EnableSameSelectTypesWithDifferentParamValues.class);

    static final AnnotationConfig NUMBER_OF_SQL_INSERT = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(InsertCountMeasureExtractor.INSTANCE)
            .perfIssueVerifier(InsertNumberPerfIssueVerifier.INSTANCE)
            .build(ExpectInsert.class);

    static final AnnotationConfig SQL_STATEMENTS_BATCHED = new AnnotationConfig.Builder()
            .perfRecorderClass(SqlStatementBatchRecorder.class)
            .perfIssueVerifier(SqlStatementBatchVerifier.INSTANCE)
            .build(ExpectJdbcBatching.class);

    static final AnnotationConfig NUMBER_OF_SQL_DELETE = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(DeleteCountMeasureExtractor.INSTANCE)
            .perfIssueVerifier(NumberOfSqlDeletePerfIssueVerifier.INSTANCE)
            .build(ExpectDelete.class);

    static final AnnotationConfig NUMBER_OF_SQL_UPDATE = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(UpdateCountMeasureExtractor.INSTANCE)
            .perfIssueVerifier(UpdateNumberPerfIssueVerifier.INSTANCE)
            .build(ExpectUpdate.class);

	static final AnnotationConfig MAX_SQL_UPDATE = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(UpdateCountMeasureExtractor.INSTANCE)
			.perfIssueVerifier(MaxOfUpdatesPerfIssueVerifier.INSTANCE)
			.build(ExpectMaxUpdate.class);

    static final AnnotationConfig MAX_SELECTED_COLUMNS = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(MaxSelectedColumnsPerMeasureExtractor.INSTANCE)
            .perfIssueVerifier(MaxSelectedColumnsPerfIssueVerifier.INSTANCE)
            .build(ExpectMaxSelectedColumn.class);

	static final AnnotationConfig MAX_UPDATED_COLUMNS = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(MaxUpdatedColumnsPerMeasureExtractor.INSTANCE)
			.perfIssueVerifier(MaxUpdatedColumnsPerfIssueVerifier.INSTANCE)
			.build(ExpectMaxUpdatedColumn.class);

    static final AnnotationConfig NUMBER_OF_SELECTED_COLUMNS = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(SelectedColumnNumberPerfMeasureExtractor.INSTANCE)
            .perfIssueVerifier(SelectedColumnNumberPerfIssueVerifier.INSTANCE)
            .build(ExpectSelectedColumn.class);

    static final AnnotationConfig DISABLE_LIKE_STARTING_WITH_WILDCARD = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(ContainsLikeWithLeadingWildcardExtractor.INSTANCE)
            .perfIssueVerifier(HasLikeWithLeadingWildcardVerifier.INSTANCE)
            .build(DisableLikeWithLeadingWildcard.class);

    static final AnnotationConfig ENABLE_LIKE_STARTING_WITH_WILDCARD = new AnnotationConfig.Builder()
            .cancelBehaviorOf(DisableLikeWithLeadingWildcard.class)
            .build(EnableLikeWithLeadingWildcard.class);
    
    static final AnnotationConfig EXPECT_MAX_QUERY_EXECUTION_TIME = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(SqlQueryExecutionTimeExtractor.INSTANCE)
			.perfIssueVerifier(SqlQueryMaxExecutionTimeVerifier.INSTANCE)
			.build(ExpectMaxQueryExecutionTime.class);

    static final AnnotationConfig EXPECT_UPDATED_COLUMN = new AnnotationConfig.Builder()
    		.perfRecorderClass(PersistenceSqlRecorder.class)
    		.perfMeasureExtractor(UpdatedColumnsMeasureExtractor.INSTANCE)
    		.perfIssueVerifier(UpdatedColumnsPerfIssueVerifier.INSTANCE)
    		.build(ExpectUpdatedColumn.class);

    static final AnnotationConfig ENABLE_QUERIES_WITHOUT_BIND_PARAMETERS = new AnnotationConfig.Builder()
			.cancelBehaviorOf(DisableQueriesWithoutBindParameters.class)
			.build(EnableQueriesWithoutBindParameters.class);

	static final AnnotationConfig DISABLE_QUERIES_WITHOUT_BIND_PARAMETERS = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
			.perfMeasureExtractor(AllParametersAreBoundExtractor.INSTANCE)
			.perfIssueVerifier(DisableQueriesWithoutBindParametersVerifier.INSTANCE)
			.build(DisableQueriesWithoutBindParameters.class);
	
    static final AnnotationConfig MAX_SQL_DELETE = new AnnotationConfig.Builder()
            .perfRecorderClass(PersistenceSqlRecorder.class)
            .perfMeasureExtractor(DeleteCountMeasureExtractor.INSTANCE)
            .perfIssueVerifier(MaxOfDeletesPerfIssueVerifier.INSTANCE)
            .build(ExpectMaxDelete.class);

	static final AnnotationConfig EXPECT_NO_CONNECTION_LEAK = new AnnotationConfig.Builder()
			.perfRecorderClass(ConnectionLeakListener.class)
			.perfIssueVerifier(ConnectionLeakVerifier.INSTANCE)
			.build(ExpectNoConnectionLeak.class);

	static final AnnotationConfig PROFILE_CONNECTION = new AnnotationConfig.Builder()
			.perfRecorderParamsExtractorFromAnnot(new ProfilingParamsExtractor())
			.perfRecorderClass(TestConnectionProfiler.class)
			.build(ProfileConnection.class);

	static final AnnotationConfig ANALYZE_SQL = new AnnotationConfig.Builder()
			.perfRecorderClass(PersistenceSqlRecorder.class)
	  		.perfMeasureExtractor(SqlAnalysisExtractor.INSTANCE)
	  		.perfIssueVerifier(AnalyzeSqlVerifier.INSTANCE)
	  		.build(AnalyzeSql.class);

}
