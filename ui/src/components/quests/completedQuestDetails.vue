<template>
    <b-list-group v-if="show === 'Objectives'">
        <h3>{{quest.title}}</h3>
        <b-list-group-item v-for="objective in quest.objectives"
                           class="flex-column align-items-start"
                           :key="objective.id">
            <div class="d-flex justify-content-between align-items-center">
                <p class="mb-1 mobile-text font-weight-bold">{{objective.riddle}}</p>
                <b-button size="sm"
                          @click="showHints(objective)"
                          variant="primary"
                          class="no-wrap-text">
                    Show Hints
                </b-button>
            </div>
            <p class="mb-1 mobile-text">
                {{objective.destination.name}}
            </p>

        </b-list-group-item>

    </b-list-group>

    <div v-else-if="show === 'Hints'">
        <b-button size="sm"
                  @click="show = 'Objectives'"
                  class="no-wrap-text">
            Back
        </b-button>
        <p class="mb-1 mobile-text font-weight-bold">Riddle: {{objective.riddle}}</p>
        <p class="mb-1 mobile-text">
            Destination: {{objective.destination.name}}
        </p>
        <list-hints
                    :objective="objective"
                    :profile="profile"
                    :hints="objective.hints"
                    :solved="true"
                    @showAddHint="showAddHint()"
                    @request-new-hints-page="getPageHints">
        </list-hints>
    </div>

    <create-hint v-else-if="show === 'Create Hint'"
                :profile="profile"
                :objective="objective"
                 @successCreate="successCreateHint"
                 @cancelCreate="cancelCreateHint">
    </create-hint>
</template>

<script>
    import CreateHint from "../hints/createHint"
    import ListHints from "../hints/listHints";

    export default {
        name: "completedQuestDetails",
        components: {ListHints, CreateHint},
        props: {
            quest: Object,
            profile: Object
        },

        data() {
            return {
                objective: Object,
                show: "Objectives",
                defaultPerPage: 5,
                defaultCurrentPage: 1
            }
        },

        watch: {
            /**
             * Watches the quest to see if it updates and if so then show the objectives.
             */
            quest() {
                this.show = "Objectives";
            }
        },

        methods: {
            /**
             * Shows the hints for the given.
             */
            showHints(objective) {
                this.show = "Hints";
                this.objective = objective;
                this.getPageHints(this.defaultCurrentPage, this.defaultPerPage);
            },


            /**
             * Shows the create hint component.
             */
            showAddHint() {
                this.show = "Create Hint";
            },


            /**
             * Successfully created the hint and display the success message to the user.
             */
            successCreateHint(responseBody) {
                this.showRewardToast(responseBody.reward);
                this.show = "Hints";
                this.objective.numberOfHints += 1;
                this.getPageHints(this.defaultCurrentPage, this.defaultPerPage);
                this.$emit("successCreate");
            },


            /**
             * When the user cancels the creation of a hint.
             */
            cancelCreateHint() {
                this.show = "Hints";
                this.getPageHints(this.defaultCurrentPage, this.defaultPerPage);
            },


            /**
             * Gets the hints to display from the backend for all hints for an objective but paginated based on
             * current page and the per page variables.
             * @param currentPage           The current viewing page.
             * @param perPage               The amount to view on a page.
             */
            getPageHints(currentPage, perPage) {
                let self = this;
                let currentPageQuery = currentPage - 1;
                fetch(`/v1/objectives/` + self.objective.id +
                    `/hints?pageNumber=` + currentPageQuery +
                    `&pageSize=` + perPage, {})
                    .then(function (response) {
                        if (!response.ok) {
                            throw response;
                        } else {
                            return response.json();
                        }
                    })
                    .then(function (responseBody) {
                        self.loadingResults = false;
                        self.objective.hints = responseBody;
                    }).catch(function (response) {
                    self.loadingResults = false;
                    self.handleErrorResponse(response);
                });
            }
        }

    }
</script>