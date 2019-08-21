<template>
    <div :class="classContainer">
        <div v-if="classContainer" >
            <h1 class="page-title">Quests</h1>
            <p class="page-title"><i>Here are all this profile's currently active quests!</i></p>
        </div>

        <b-row>
            <b-col cols="12" md="8">
                <b-list-group>
                    <!--Successful quest alert -->
                    <b-alert
                            :show="dismissCountDown"
                            @dismiss-count-down="countDownChanged"
                            @dismissed="dismissCountDown=0"
                            dismissible
                            variant="success">
                        <p>{{alertText}}</p>
                        <b-progress
                                :max="dismissSeconds"
                                :value="dismissCountDown"
                                height="4px"
                                variant="success"
                        ></b-progress>
                    </b-alert>
                    <b-list-group-item href="#" class="flex-column justify-content-center"
                                       v-if="creatingQuest"
                                       draggable="false">
                        <quest-item
                                :selected-objective="selectedObjective"
                                :heading="'Create'"
                                :profile="profile"
                                @successCreate="showSuccess"
                                @cancelCreate="cancelCreate"
                                :selectedDestination="destinationSelected"
                                @OBJ-side-bar="showHideBar => this.showDestinations = showHideBar"
                                @Your-OBJ-side-bar="showHideBar => this.showYourObjectives = showHideBar"
                        ></quest-item>
                    </b-list-group-item>
                    <b-list-group-item href="#" class="flex-column justify-content-center"
                                       v-if="editingQuest"
                                       draggable="false">
                        <quest-item
                                :selected-objective="selectedObjective"
                                :inputQuest="copiedQuest"
                                :heading="'Edit'"
                                :profile="profile"
                                @successEdit="successEdit"
                                @cancelCreate="cancelEdit"
                                :selectedDestination="destinationSelected"
                                @OBJ-side-bar="showHideBar => this.showDestinations = showHideBar"
                                @Your-OBJ-side-bar="showHideBar => this.showYourObjectives = showHideBar"
                        ></quest-item>
                    </b-list-group-item>
                    <div v-if="yourQuests">
                        <b-list-group-item href="#" class="flex-column justify-content-center"
                                           v-if="!creatingQuest && !editingQuest"
                                           draggable="false">
                            <div class="d-flex justify-content-center">
                                <b-button variant="success" @click="addQuest" block>Add a New Quest</b-button>
                            </div>
                        </b-list-group-item>
                    </div>
                    <b-list-group-item v-for="quest in foundQuests" href="#"
                                       class="flex-column align-items-start"
                                       :key="quest.id"
                                       draggable="false"
                                        v-if="!activeQuests"
                                       @click="selectedQuest = quest">
                        <template v-if="!editingQuest && !(activeId === quest.id)">
                            <b-row class="buttonMarginsTop">
                                <b-col :cols="availableQuests ? 5 : ''">
                                    <h4>Title</h4>
                                    <p>{{quest.title}}</p>
                                </b-col>
                                <b-col>
                                    <h4>Countries</h4>
                                    <p>{{getQuestCountries(quest)}}</p>
                                </b-col>
                            </b-row>
                            <b-row class="buttonMarginsTop">
                                <b-col>
                                    <h4>Start Date</h4>
                                    {{new Date(quest.startDate)}}
                                </b-col>
                                <b-col>
                                    <h4>End Date</h4>
                                    {{new Date(quest.endDate)}}
                                </b-col>
                                <!-- If looking at the available quests tab, show a 'start now' button -->
                                <b-col cols="2" v-if="availableQuests">
                                    <b-row>
                                        <b-button variant="primary" @click="createAttempt(quest, true)">Start Now</b-button>
                                    </b-row>
                                    <b-row class="mt-4">
                                        <b-button variant="secondary" @click="createAttempt(quest, false)">Start Later</b-button>
                                    </b-row>
                                </b-col>
                            </b-row>
                            <div v-if="yourQuests" class="buttonMarginsTop">
                                <b-button @click="showHideLocations(quest)" variant="primary" class="buttonMarginsBottom">
                                    Show/Hide Locations
                                </b-button>
                                <b-container fluid style="margin-top: 20px; display: none" :id="'display-' + quest.id">
                                    <!-- Table displaying all quest objectives -->
                                    <b-table :current-page="currentPage" :fields="fields" :items="quest.objectives"
                                             :per-page="perPage"
                                             hover
                                             id="myTrips"
                                             outlined
                                             ref="questObjective"
                                             striped>

                                    <template slot="radius" slot-scope="row">
                                        {{getRadiusValue(row.item.radius)}}
                                    </template>
                                </b-table>
                                <!-- Determines pagination and number of results per row of the table -->
                                <b-row>
                                    <b-col cols="2">
                                        <b-form-group
                                                id="numItems-field"
                                                label-for="perPage">
                                            <b-form-select :options="optionViews"
                                                           id="perPage"
                                                           size="sm"
                                                           trim v-model="perPage">
                                            </b-form-select>
                                        </b-form-group>
                                    </b-col>
                                    <b-col>
                                        <b-pagination
                                                :per-page="perPage"
                                                :total-rows="rows(quest)"
                                                align="center"
                                                aria-controls="my-table"
                                                first-text="First"
                                                last-text="Last"
                                                size="sm"
                                                v-model="currentPage">
                                        </b-pagination>
                                    </b-col>
                                </b-row>
                            </b-container>
                        </div>

                            <b-row v-if="yourQuests">
                                <b-col>
                                    <b-button variant="warning" @click="setActiveId(quest)" block>Edit</b-button>
                                </b-col>
                                <b-col>
                                    <b-button variant="danger" @click="setQuest(quest)" block>Delete
                                    </b-button>
                                </b-col>
                            </b-row>
                        </template>
                        <!--Quest component-->
                    </b-list-group-item>
                </b-list-group>
                <!-- Confirmation modal for deleting a quest. -->
                <b-modal hide-footer id="deleteQuestModal" ref="deleteQuestModal" title="Delete Quest">
                    <div v-if="activeUsers > 0"
                         class="d-block">
                        This quest is used by {{activeUsers}} users.
                    </div>
                    <div class="d-block">
                        Are you sure that you want to delete this Quest?
                    </div>
                    <!--Error when deleting quest alert-->
                    <b-alert
                            v-model="deleteAlertError"
                            dismissible
                            variant="danger">
                        <p>{{deleteAlertMessage}}</p>
                    </b-alert>
                    <b-button
                            class="mr-2 float-right"
                            variant="danger"
                            @click="deleteQuest">Delete
                    </b-button>
                    <b-button
                            @click="dismissModal('deleteQuestModal')"
                            class="mr-2 float-right">Cancel
                    </b-button>
                </b-modal>
                <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults">
                    <div class="d-flex justify-content-center">
                        <b-spinner></b-spinner>
                    </div>
                </b-list-group-item>
                <b-list-group-item href="#" class="flex-column justify-content-center"
                                   v-if="!loadingResults && foundQuests.length === 0">
                    <div class="d-flex justify-content-center">
                        <strong>No Quests Found</strong>
                    </div>
                </b-list-group-item>
            </b-col>
            <b-col cols="12" md="4">
                <b-card class="d-none d-lg-block" v-if="!hideSideBar">
                    <found-destinations
                            v-if="showDestinations"
                            :search-public="true"
                            :profile="profile"
                            @destination-click="destination => this.destinationSelected = destination">
                    </found-destinations>
                    <objective-list
                            v-if="showYourObjectives"
                            :yourObjectives="true"
                            :profile="profile"
                            :sideBarView="true"
                            @select-objective="setSelectedObjective">
                    </objective-list>
                    <quest-search-form
                            v-if="availableQuests"
                            :profile="profile"
                            @searched-quests="quests => this.foundQuests = quests">
                    </quest-search-form>
                    <completed-quest-details
                            v-if="completedQuests"
                            :quest="selectedQuest">
                    </completed-quest-details>
                </b-card>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import QuestItem from "./questItem";
    import FoundDestinations from "../destinations/destinationSearchList";
    import ObjectiveList from "../objectives/objectiveList";
    import QuestSearchForm from "./questSearchForm";
    import QuestAttemptSolve from "./activeQuestSolve";
    import ActiveQuestList from "./activeQuestPage";
    import CompletedQuestDetails from "./completedQuestDetails";

    export default {
        name: "questList",

        props: {
            profile: Object,
            adminView: {
                default: function () {
                    return false;
                }
            },
            yourQuests: {
                default: function () {
                    return false;
                }
            },
            completedQuests: {
                default: function () {
                    return false;
                }
            },
            availableQuests: {
                default: function () {
                    return false;
                }
            },
            activeQuests: {
                default: function () {
                    return false;
                }
            },
            selectedDestination: {},
            refreshQuests: Boolean,
            hideSideBar: {
                default: function () {
                    return false;
                }
            },
            classContainer: {
                default: function () {
                    return "";
                }
            }
        },

        data() {
            return {
                foundQuests: [],
                loadingResults: true,
                moreResults: true,
                creatingQuest: false,
                editingQuest: false,
                activeId: 0,
                questId: null,
                dismissSeconds: 3,
                dismissCountDown: 0,
                alertText: "",
                copiedQuest: null,
                deleteAlertError: false,
                deleteAlertMessage: "",
                showDestinations: false,
                showYourObjectives: false,
                showQuestAttemptSolve: false,
                selectedObjectiveTemplate: {
                    id: null,
                    destination: null,
                    riddle: "",
                    radius: null
                },
                selectedObjective: {
                    id: null,
                    destination: null,
                    riddle: "",
                    radius: null
                },
                destinationSelected: {},
                perPage: 5,
                currentPage: 1,
                showLocations: false,
                fields: [
                    {key: 'riddle', label: 'Riddle'},
                    {key: 'destination.name', label: 'Destination'},
                    {key: 'radius', label: 'Radius'}
                ],
                optionViews: [
                    {value: 1, text: "1"},
                    {value: 5, text: "5"},
                    {value: 10, text: "10"},
                    {value: 15, text: "15"},
                    {value:Infinity, text:"All"}],
                questAttempts: [],
                selectedQuestAttempt: {},
                selectedQuest: {},
                activeUsers: 0
            }
        },

        mounted() {
            this.getMore();
        },

        watch: {
            refreshQuests() {
                this.getMore();
            },

            profile() {
                this.getMore();
            }
        },

        methods: {
            /**
             * Used to convert the quest object into a Json object.
             */
            copyQuest(quest) {
                this.copiedQuest = JSON.parse(JSON.stringify(quest))
            },


            /**
             * Function to retrieve more quests when a user reaches the bottom of the list.
             */
            getMore() {
                this.foundQuests = [];
                if (this.yourQuests) {
                    this.queryYourQuests();
                } else if(this.completedQuests) {
                    this.queryCompletedQuests();
                } else {
                    this.queryQuests();
                }
            },


            /**
             * Send the Http request to delete the specified Quest.
             */
            deleteQuest() {
                let self = this;
                fetch('/v1/quests/' + this.questId, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.ok) {
                        self.getMore();
                        self.$refs['deleteQuestModal'].hide();
                        self.alertText = "Quest Successfully Deleted";
                        self.showAlert();
                    }
                    else {
                        // Converts response to text, this is then displayed on the frontend.
                        let responseBody = JSON.parse(data);
                        let message = "";
                        for (let i = 0; i < responseBody.length; i++) {
                            message += responseBody[i].message + "\n";
                        }
                        self.deleteAlertMessage = message;
                        self.deleteAlertError = true;
                    }
                });
            },


            /**
             * Runs a query which searches through the quests in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryQuests() {
                this.loadingResults = true;
                return fetch('/v1/quests', {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.foundQuests = data;
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests created by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourQuests() {
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    return fetch(`/v1/quests/` + this.profile.id, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.foundQuests = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests started by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourActiveQuests() {
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    return fetch(`/v1/quests/profiles/` + this.profile.id, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.questAttempts = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Creates a new quest attempt for the selected quest and current user.
             *
             * @returns {Promise<Response | never>}
             */
            createAttempt(questToAttempt, viewActive) {
                if (this.profile.id !== undefined) {
                    return fetch(`/v1/quests/` + questToAttempt.id + `/attempt/` + this.profile.id, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                        }
                        return response;
                    }).then(response => response.json())
                        .then(data => {
                            // If 'start now' is clicked
                            if (viewActive) {
                                this.$emit('start-quest-now', data);
                            } else {
                                // Refresh quests
                                this.getMore();
                                this.showSuccess("Quest started");
                                this.$emit('start-quest-later', data);
                            }
                        });
                }
            },


            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests created by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryCompletedQuests() {
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    return fetch(`/v1/quests/` + this.profile.id + `/complete`, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.foundQuests = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Changes creatingQuest to true to show the create quest window, and calls function to close edit
             * windows.
             */
            addQuest() {
                this.creatingQuest = true;
                this.cancelEdit();
            },


            /**
             * Changes the active quest id to the inputted one, and sets creatingQuest to false to hide creation
             * box.
             * @param quest     the quest to be changed to.
             */
            setActiveId(quest) {
                this.copyQuest(quest);
                this.activeId = quest.id;
                this.creatingQuest = false;
                this.editingQuest = true;
            },


            /**
             * Changes the quest id to the currently selected quest id.
             * Dismisses the delete quest modal.
             *
             */
            setQuest(quest) {
                this.questId = quest.id;

                this.getActiveUsers();
                this.$refs['deleteQuestModal'].show();
            },


            /**
             * Gets all users that are currently using the given quest.
             */
            getActiveUsers() {
                let self = this;
                return fetch('/v1/quests/' + this.questId + '/profiles', {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(data => {
                        self.activeUsers = data.length;
                    });
            },


            /**
             * Sets editingQuest to false and the active quest ID to 0 to close any open quest editing box.
             */
            cancelEdit() {
                this.getMore();
                this.editingQuest = false;
                this.activeId = 0;
                this.showDestinations = false;
            },


            /**
             * Displays success message and hides all edit fields.
             */
            successEdit() {
                this.getMore();
                this.editingQuest = false;
                this.activeId = 0;
                this.showDestinations = false;
                this.alertText = "Quest successfully edited";
                this.showAlert();
            },


            /**
             * Sets creatingQuest to false and emits signal to hide destination search box. clears selected destination.
             */
            cancelCreate() {
                this.getMore();
                this.creatingQuest = false;
                this.showDestinations = false;
            },


            /**
             * Sets the message for the success alert to the inputted message and runs showAlert to show the success
             * message.
             * @param message to be set as the alert message.
             */
            showSuccess(message) {
                this.getMore();
                this.queryYourQuests();
                this.alertText = message;
                this.showAlert();
            },


            /**
             * Converts the Http response body to a Json.
             * @param response  the received Http response.
             * @returns {*}     the response body as a Json object.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Used to dismiss the delete a quest confirmation modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
                this.deleteAlertError = false;
            },


            /**
             * Displays the countdown alert on the successful deletion of a quest.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSeconds;
            },


            /**
             * Used to countdown the progress bar on an alert to countdown.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown;
            },


            /**
             * Sets the treasure hunt emitted from the select treasure hunt side bar.
             */
            setSelectedObjective(objective) {
                let newObjective = JSON.parse(JSON.stringify(objective));
                let radius = newObjective.radius;
                let radiusValue;
                let radiusList = [
                    {value: 0.005, text: "5 Meters"},
                    {value: 0.01, text: "10 Meters"},
                    {value: 0.02, text: "20 Meters"},
                    {value: 0.05, text: "50 Meters"},
                    {value: 0.1, text: "100 Meters"},
                    {value: 0.5, text: "500 Meters"},
                    {value: 1, text: "1 Km"},
                    {value: 5, text: "5 Km"},
                    {value: 10, text: "10 Km"},
                ];
                for (let i = 0; i < radiusList.length; i++) {
                    if (radius === radiusList[i].value) {
                        radiusValue = radiusList[i];
                    }
                }
                newObjective.radius = radiusValue;
                this.selectedObjective = newObjective;
            },


            /**
             * Returns a string radius value determined by the size.
             *
             * @param radius    the radius to be changed.
             */
            getRadiusValue(radius) {
                if (radius < 1) {
                    return radius * 1000 + " Meters"
                }
                return radius + " Km";
            },


            /**
             * Returns a string of the countries contained in the quest objectives, which indicates the quest scope.
             *
             * @param quest     the quest containing one or more countries.
             */
            getQuestCountries(quest) {
                let countries = "";
                let numberOfCountries = quest.objectiveCountries.length;
                for (let i = 0; i < numberOfCountries - 1; i++) {
                    countries += quest.objectiveCountries[i] + ", ";
                }
                countries += quest.objectiveCountries[numberOfCountries - 1];

                return countries;
            },


            /**
             * Computed function used for the pagination of the table.
             *
             * @returns {number}    the number of rows required in the table based on number of objectives to be
             *                      displayed.
             */
            rows(quest) {
                return quest.objectives.length
            },


            /**
             * Hides or shows the quest locations given by the quest location id parameter.
             *
             * @param quest      the quest locations to hide.
             */
            showHideLocations(quest) {
                let questLocationsId = "display-" + quest.id;
                let locationsSection = document.getElementById(questLocationsId);

                if (locationsSection.style.display === "none") {
                    locationsSection.style.display = "block";
                    this.checkShowHide(quest);
                } else {
                    locationsSection.style.display = "none";
                    this.checkShowHide(quest);
                }
            }
        },

        components: {
            CompletedQuestDetails,
            ActiveQuestList,
            QuestAttemptSolve,
            ObjectiveList,
            QuestItem,
            FoundDestinations,
            QuestSearchForm
        }
    }
</script>