<template>
    <b-list-group v-if="!showHintSideBar">
        <h3>{{quest.title}}</h3>
        <b-list-group-item v-for="objective in quest.objectives"
                           class="flex-column align-items-start"
                           :key="objective.id">
            <div class="d-flex justify-content-between align-items-center">
                <p class="mb-1 mobile-text font-weight-bold">{{objective.riddle}}</p>
                <b-button size="sm"
                          @click="addHint(objective)"
                          variant="primary"
                          class="no-wrap-text">
                    Add Hint
                </b-button>
            </div>
            <p class="mb-1 mobile-text">
                {{objective.destination.name}}
            </p>

        </b-list-group-item>

    </b-list-group>
    <create-hint v-else
                :profile="profile"
                :objective="objective"
                 @successCreate="successCreateHint"
                 @cancelCreate="showHintSideBar = false">
    </create-hint>
</template>

<script>
    import CreateHint from "../hints/createHint"

    export default {
        name: "completedQuestDetails",
        components: {CreateHint},
        props: {
            quest: Object,
            profile: Object,
            showHintSideBar: false
        },

        data() {
            return {
                objective: Object
            }
        },

        methods: {
            /**
             * Sends the emit to the quest list to view the add hint side bar
             */
            addHint(objective) {
                this.showHintSideBar = true;
                this.objective = objective;
            },


            /**
             * Successfully created the hint and display the success message to the user.
             */
            successCreateHint() {
                this.$emit("successCreate");
            }
        }

    }
</script>