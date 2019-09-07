<template>
    <div>
        <b-list-group>
            <b-list-group-item v-for="questAttempt in questAttempts" href="#"
                               class="flex-column align-items-start"
                               :key="questAttempt.id"
                               draggable="false"
                               @click="$emit('quest-attempt-clicked', questAttempt)">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">{{questAttempt.questAttempted.title}}</h5>
                </div>
                <div class="d-flex w-100 justify-content-center">
                    <p>{{new Date(questAttempt.questAttempted.startDate).toLocaleDateString()}} &rarr;
                        {{new Date(questAttempt.questAttempted.endDate).toLocaleDateString()}}</p>
                </div>

                <p class="mb-1">
                    <b-progress
                            :value="questAttempt.progress"
                            :max="100"
                            :variant="(questAttempt.completed ? 'success' : 'primary')"
                            class="mb-3">
                    </b-progress>
                </p>
            </b-list-group-item>

            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults">
                <div class="d-flex justify-content-center">
                    <b-img alt="Loading" class="align-middle loading" src="../../../static/logo.png" width="50%"></b-img>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center"
                               v-if="!loadingResults && questAttempts.length === 0">
                <div class="d-flex justify-content-center">
                    <strong>No Quests Found</strong>
                </div>
            </b-list-group-item>
        </b-list-group>
    </div>
</template>

<script>
    export default {
        name: "activeQuestList",

        props: {
            questAttempts: Array,
            loadingResults: Boolean,
            viewingFromProfilePage: {
                default() {
                    return false;
                }
            }
        }
    }
</script>