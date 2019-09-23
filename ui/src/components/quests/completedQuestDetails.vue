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
                    @showAddHint="showAddHint()">
        </list-hints>
    </div>

    <create-hint v-else-if="show === 'Create Hint'"
                :profile="profile"
                :objective="objective"
                 @successCreate="successCreateHint"
                 @cancelCreate="show = 'Hints'">
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
                show: "Objectives"
            }
        },

        watch: {
            quest() {
                this.show = "Objectives";
            }
        },

        methods: {
            /**
             * Shows the hints for the given
             */
            showHints(objective) {
                this.show = "Hints";
                this.objective = objective;
            },


            showAddHint() {
                this.show = "Create Hint";
            },


            /**
             * Successfully created the hint and display the success message to the user.
             */
            successCreateHint(responseBody) {
                this.showRewardToast(responseBody.reward);
                this.show = "Hints";
                this.objective.hints.push(responseBody.newHint);
                this.$emit("successCreate");
            }
        }

    }
</script>