<template>
    <div>
        <h1 class="page-title">{{ heading }} a Quest!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <b-alert dismissible v-model="showSuccessTreasureHunt" variant="success">{{successMessage}}</b-alert>

        <!-- Displays success alert and progress bar on quest creation as a loading bar
        for the quest being added to the database -->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Quest Successfully Saved</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <b-row>
            <b-col>
                <b-form>

                    <b-container fluid>
                        <b-form-group
                                id="quest_title-field"
                                label="Quest Title:"
                                label-for="quest_title">
                            <b-form-textarea :type="'expandable-text'"
                                             id="quest_title"
                                             trim
                                             v-model="inputQuest.title"
                                             :state="validateTitle">
                            </b-form-textarea>
                        </b-form-group>
                    </b-container>
                    <b-form>
                        <b-container fluid>
                            <b-form-group
                                    id="startDate-field">
                                <b-row>
                                    <b-col cols="6">
                                        <label>Start Date:</label>
                                        <b-form-input :type="'date'"
                                                      id="startDate"
                                                      min='getCurrentDate()'
                                                      max='9999-12-31'
                                                      trim
                                                      v-model="inputQuest.startDate"
                                                      :state="validateStartDate">

                                        </b-form-input>
                                    </b-col>
                                    <b-col cols="6">
                                        <label>Start Time:</label>
                                        <b-form-input :type="'time'"
                                                      id="startTime"
                                                      min='getCurrentTime()'
                                                      max=''
                                                      trim
                                                      v-model="startTime"
                                                      :state="validateStartTime">
                                        </b-form-input>
                                    </b-col>
                                </b-row>
                            </b-form-group>
                            <b-form-group
                                    id="endDate-field">
                                <b-col cols="6"></b-col>
                                <b-col cols="6"></b-col>

                                <b-row>
                                    <b-col cols="6">
                                        <label>Expiration Date:</label>
                                        <b-form-input :type="'date'"
                                                      id="endDate"
                                                      min='getCurrentDate()'
                                                      max='9999-12-31'
                                                      trim
                                                      v-model="inputQuest.endDate"
                                                      :state="validateEndDate">

                                        </b-form-input>
                                    </b-col>

                                    <b-col cols="6">
                                        <label>Expiration Time:</label>
                                        <b-form-input :type="'time'"
                                                      id="endTime"
                                                      min='getCurrentTime()'
                                                      max=''
                                                      trim
                                                      v-model="endTime"
                                                      :state="validateEndTime">
                                        </b-form-input>
                                    </b-col>
                                </b-row>
                            </b-form-group>
                        </b-container>
                    </b-form>
                </b-form>

                <div v-if="!editCurrentTreasureHunt">
                    <b-row>
                        <b-col>
                            <b-button v-if="!addNewTreasureHunt" variant="success"
                                      @click="showTreasureHuntComponent" block>
                                Add a New Treasure Hunt
                            </b-button>
                        </b-col>
                        <b-col>
                            <b-button v-if="!addNewTreasureHunt" variant="primary"
                                      @click="showYourTreasureHuntsComponent" block>
                                Select a Treasure Hunt
                            </b-button>
                        </b-col>
                    </b-row>
                </div>
                <b-row v-if="addNewTreasureHunt">
                    <b-col cols="12">
                        <b-card>
                        <add-treasure-hunt
                                :inputTreasureHunt="treasureHuntSelected"
                                :profile="profile"
                                :heading="'Add'"
                                @addTreasureHunt="addTreasureHunt"
                                @cancelCreate="cancelTreasureHuntCreate"
                                @destination-select="$emit('destination-select')"
                                :selectedDestination="destinationSelected">
                        </add-treasure-hunt>
                        </b-card>
                    </b-col>
                </b-row>
                <b-row v-if="editCurrentTreasureHunt">
                    <b-col cols="12">
                        <b-card>
                            <add-treasure-hunt
                                    :inputTreasureHunt="treasureHuntSelected"
                                    :profile="profile"
                                    :heading="'Edit'"
                                    @addTreasureHunt="addTreasureHunt"
                                    @editTreasureHunt="treasureHuntEdited"
                                    @cancelCreate="cancelTreasureHuntCreate"
                                    @destination-select="$emit('destination-select')"
                                    :selectedDestination="destinationSelected">
                            </add-treasure-hunt>
                        </b-card>
                    </b-col>
                </b-row>

                <b-container fluid style="margin-top: 20px" v-if="inputQuest.treasureHunts.length > 0">
                    <!-- Table displaying all added destinations -->
                    <b-table :current-page="currentPage" :fields="fields" :items="inputQuest.treasureHunts"
                             :per-page="perPage"
                             hover
                             id="myTrips"
                             outlined
                             ref="questTreasureHunt"
                             striped>

                        <!-- Buttons that appear for each treasure hunt added to table -->
                        <template slot="actions" slot-scope="row">
                            <b-button size="sm"
                                      @click="editTreasureHunt(row.index)"
                                      variant="warning"
                                      class="mr-2"
                                      block>Edit
                            </b-button>
                            <!--Removes treasure hunt from table-->
                            <b-button size="sm"
                                      @click="deleteTreasureHunt(row.index)"
                                      variant="danger"
                                      class="mr-2"
                                      block>Delete
                            </b-button>
                        </template>

                        <!-- Buttons to shift destinations up/down in table -->
                        <template slot="order" slot-scope="row">
                            <b-button :disabled="inputQuest.treasureHunts.length === 1 || row.index === 0"
                                      @click="moveUp(row.index)"
                                      class="mr-2"
                                      size="sm"
                                      variant="success">&uarr;
                            </b-button>
                            <b-button :disabled="inputQuest.treasureHunts.length === 1 ||
                           row.index === inputQuest.treasureHunts.length-1"
                                      @click="moveDown(row.index)"
                                      class="mr-2"
                                      size="sm"
                                      variant="success">&darr;
                            </b-button>
                        </template>
                        <template slot="radius" slot-scope="row">
                            {{getRadiusValue(row.item.radius)}}
                        </template>
                    </b-table>
                    <!-- Determines pagination and number of results per row of the table -->
                    <b-row>
                        <b-col cols="1">
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
                        <b-col cols="8">
                            <b-pagination
                                    :per-page="perPage"
                                    :total-rows="rows"
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

                <b-row class="buttonMarginsTop">
                    <b-col>
                        <b-button @click="validateQuest" block variant="primary">
                            Save
                        </b-button>
                    </b-col>
                    <b-col>
                        <b-button @click="cancelCreate" block>
                            Cancel
                        </b-button>
                    </b-col>
                </b-row>
            </b-col>
        </b-row>

    </div>
</template>

<script>
    import BCol from "bootstrap-vue/es/components/layout/col";
    import AddTreasureHunt from "../treasureHunt/treasureHuntItem";

    export default {
        name: "questItem",

        components: {
            BCol,
            AddTreasureHunt
        },

        props: {
            profile: Object,
            inputQuest: {
                default: function () {
                    return {
                        id: null,
                        title: "",
                        startDate: "",
                        endDate: "",
                        treasureHunts: []
                    }
                }
            },
            newDestination: Object,
            selectedDestination: {
                default: function () {
                    return {}
                }
            },
            heading: String,
            containerClass: {
                default: function () {
                    return 'containerWithNav';
                }
            },
            selectedTreasureHunt: {
                default: function () {
                    return {}
                }
            }
        },

        data() {
            return {
                showError: false,
                showDateError: false,
                errorMessage: "",
                dismissSecs: 3,
                dismissCountDown: 0,
                savingTreasureHunt: false,
                letTreasureHuntSaved: false,
                startTime: "",
                endTime: "23:59",
                displayedDestination: null,
                addNewTreasureHunt: false,
                showSuccessTreasureHunt: false,
                successMessage: '',
                fields: [
                    'order',
                    {key: 'riddle', label: 'Riddle'},
                    {key: 'destination.name', label: 'Destination'},
                    {key: 'radius', label: 'Radius'},
                    'actions'
                ],
                optionViews: [{value: 1, text: "1"}, {value: 5, text: "5"}, {value: 10, text: "10"}, {
                    value: 15,
                    text: "15"
                }],
                editCurrentTreasureHunt: false,
                treasureHuntIndex: 0,
                treasureHuntSelected: {
                    id: null,
                    destination: null,
                    riddle: "",
                    radius: null
                },
                destinationSelected: {},
                treasureHuntTemplate: {
                    id: null,
                    destination: null,
                    riddle: "",
                    radius: null
                }
            }
        },

        watch: {
            selectedDestination() {
                this.destinationSelected = this.selectedDestination;
                this.inputQuest.destination = this.selectedDestination;
                this.displayedDestination = this.selectedDestination;
            },

            selectedTreasureHunt() {
                this.treasureHuntSelected = this.selectedTreasureHunt;
            }
        },

        mounted() {
            this.splitDates();
            this.setDateTimeString();
        },

        computed: {
            /**
             * For new quest, checks the start date is after the current date.
             * For all other quests, checks the start date is either the same as or before the end date.
             *
             * @returns {boolean} true if start date is valid.
             */
            validateStartDate() {
                // For a new hunt, the start date must be after today.
                if ((this.inputQuest.startDate < this.getDateString() && !this.inputQuest.id)) {
                    return false;
                }
                // Otherwise, checks the start date is equal to or before the end date.
                return this.inputQuest.startDate <= this.inputQuest.endDate;
            },


            /**
             * Checks that the start time is not after or the same as the end time if the dates are the same,
             * and that the start time is not before the current time if the current date is today.
             *
             * @returns {boolean} true if start time is valid.
             */
            validateStartTime() {
                // For new quests, check the start time is after the current time.
                if (this.startTime === "" || this.startTime === undefined) {
                    return false
                }
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.startTime >= this.endTime) {
                        return false;
                    }
                }
                // If the dates are the same, check the start time is before the end time.
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.startTime >= this.endTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * Checks that the end time is not before or the same as the start time if the dates are the same.
             *
             * @returns {boolean} true if end time is valid.
             */
            validateEndTime() {
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.endTime <= this.startTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * For new quests, checks the end date is after the current date.
             * For all other quests, checks the end date is either the same as or after the start date.
             *
             * @returns {boolean} true if end date is valid.
             */
            validateEndDate() {
                // For a new quests, the end date must be after today.
                if (this.inputQuest.endDate < this.getDateString() && !this.inputQuest.id) {
                    return false;
                }
                // Otherwise, checks the end date is equal to or after the start date.
                return this.inputQuest.endDate >= this.inputQuest.startDate;
            },


            /**
             * Returns true if the inputted title has length greater than 0.
             *
             * @returns {Boolean} true if validated.
             */
            validateTitle() {
                if (this.inputQuest.title.length > 0) {
                    return true;
                }
                return null;
            },


            /**
             * Computed function used for the pagination of the table.
             *
             * @returns {number}    the number of rows required in the table based on number of treasure hunts to be
             *                      displayed.
             */
            rows() {
                return this.inputQuest.treasureHunts.length
            }
        },

        methods: {
            /**
             * Gets the current date+time as a Date object.
             *
             * @returns Current Datetime.
             */
            getCurrentDate() {
                return new Date();
            },


            /**
             * sets the input values to be their proper string versions of current date/time.
             */
            setDateTimeString() {
                if (this.inputQuest.id === null) {
                    this.inputQuest.startDate = this.getDateString();
                    this.inputQuest.endDate = this.getDateString();
                    this.startTime = this.getTimeString();
                }
            },


            /**
             * Gets the current date as a string in YYYY-MM-DD format, including padding O's on month/day.
             *
             * @returns Current Date in YYYY-MM-DD String Format.
             */
            getDateString() {
                let today = this.getCurrentDate();
                return today.getFullYear() + '-' +
                    ((today.getMonth() + 1) < 10 ? "0" : "")
                    + (today.getMonth() + 1) + '-' +
                    (today.getDate() < 10 ? "0" : "") +
                    today.getDate();
            },


            /**
             * Gets the current time as a string in HH:MM format, including padding O's.
             *
             * @returns Current Time in HH:MM String Format.
             */
            getTimeString() {
                let today = this.getCurrentDate();
                return ((today.getHours()) < 10 ? "0" : "") +
                    today.getHours() + ":"
                    + ((today.getMinutes()) < 10 ? "0" : "") +
                    today.getMinutes();
            },


            /**
             * If all field validations pass on the active quest, saves the quest using either
             * updateQuest if there is an active editing ID or saveQuest otherwise (adding a new one).
             */
            validateQuest() {
                if (this.validateStartDate && this.validateStartTime && this.validateEndDate && this.validateEndTime
                    && this.validateTitle) {
                    if (this.inputQuest.id !== null) {
                        this.updateQuest();
                    } else {
                        this.saveQuest();
                    }
                } else {
                    this.errorMessage = "Not all fields have valid information!";
                    this.showError = true;
                }

            },


            /**
             * Creates formatted JSON of the currently active quest.
             *
             * @returns JSON string with fields 'title', 'treasureHunts', 'startDate', 'endDate'.
             */
            assembleQuest() {
                this.joinDates();
            },


            /**
             * POST's the currently active quest to the quests endpoint in JSON format, for newly creating
             * quests.
             */
            saveQuest() {
                this.assembleQuest();
                let self = this;
                fetch('/v1/quests/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputQuest)
                }).then(function (response) {
                    if (response.status >= 400) {
                        // Ensures the start and end date fields are not wiped after an error occurs.
                        this.splitDates();
                        // Converts response to text, this is then displayed on the frontend.
                        response.text().then(data => {
                            self.errorMessage = data;
                            self.showError = true;
                        });
                    } else {
                        self.$emit('successCreate', "Quest Successfully Created");
                        self.$emit('cancelCreate')
                    }
                });
            },


            /**
             * PUT's the currently active quest to the quests endpoint in JSON format, for edited
             * quests.
             */
            updateQuest() {
                this.assembleQuest();
                let self = this;
                fetch('/v1/quests/' + this.inputQuest.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputQuest)
                }).then(function (response) {
                    if (response.status >= 400) {
                        // Ensures the start and end date fields are not wiped after an error occurs.
                        this.splitDates();
                        // Converts response to text, this is then displayed on the frontend.
                        response.text().then(data => {
                            self.errorMessage = data;
                            self.showError = true;
                        });
                    } else {
                        self.$emit('cancelCreate')
                    }
                });
            },


            /**
             * Adds the specified treasure hunt to the list of quest treasure hunts and handles the appropriate actions.
             */
            addTreasureHunt(treasureHunt) {
                this.inputQuest.treasureHunts.push(JSON.parse(JSON.stringify(treasureHunt)));
                this.treasureHuntSelected = this.treasureHuntTemplate;
                this.destinationSelected = {};
                this.successMessage = "Treasure Hunt Successfully Added";
                this.showSuccessTreasureHunt = true;
                let self = this;
                setTimeout(function () {
                    self.showSuccessTreasureHunt = false;
                }, 3000);
                this.$emit('TH-side-bar', false)
            },


            /**
             * Replaces the treasure hunt in the quest treasure hunts array with the newly edited treasure hunt.
             */
            treasureHuntEdited(treasureHunt) {
                this.inputQuest.treasureHunts[this.treasureHuntIndex] = JSON.parse(JSON.stringify(treasureHunt));
                this.treasureHuntSelected = this.treasureHuntTemplate;
                this.destinationSelected = {};
                this.successMessage = "Treasure Hunt Successfully Edited";
                this.showSuccessTreasureHunt = true;
                let self = this;
                setTimeout(function () {
                    self.showSuccessTreasureHunt = false;
                }, 3000);
                this.$refs.questTreasureHunt.refresh();
            },


            /**
             * Displays the edit treasure hunt field and sets the current treasure hunt to the specified value.
             */
            editTreasureHunt(rowIndex) {
                this.treasureHuntIndex = rowIndex;
                let radius = this.inputQuest.treasureHunts[rowIndex].radius;
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
                this.treasureHuntSelected = JSON.parse(JSON.stringify(this.inputQuest.treasureHunts[rowIndex]));
                this.treasureHuntSelected.radius = radiusValue;
                this.$emit('TH-side-bar', true);
                this.destinationSelected = JSON.parse(JSON.stringify(this.inputQuest.treasureHunts[rowIndex].destination));
                this.editCurrentTreasureHunt = true;
            },


            /**
             * Removes a treasure hunt from the list of quest's treasure hunts.
             */
            deleteTreasureHunt(rowIndex) {
                this.inputQuest.treasureHunts.splice(rowIndex, 1);
                this.showSuccessTreasureHunt = false;
                this.successMessage = "Treasure Hunt Successfully Deleted";
                this.showSuccessTreasureHunt = true;
                let self = this;
                setTimeout(function () {
                    self.showSuccessTreasureHunt = false;
                }, 3000);
            },


            /**
             * Cancels the creation or editing of a quest by emitting a value to the questList.
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * Cancels the current creation of a treasure hunt addition to a quest
             */
            cancelTreasureHuntCreate() {
                this.addNewTreasureHunt = false;
                this.editCurrentTreasureHunt = false;
                this.$emit('clear-treasure-hunt-values');
                this.$emit('TH-side-bar', false);
                this.$emit('Your-TH-side-bar', false);
            },


            /**
             * Splits the dates of the quest to put in the edit fields.
             */
            splitDates() {
                if (this.inputQuest.id !== null) {
                    this.inputQuest.startDate = new Date(this.inputQuest.startDate).toLocaleString();
                    let startDate = this.quest.startDate;
                    // The date is the values before the comma
                    this.inputQuest.startDate = this.inputQuest.startDate.split(", ")[0];
                    // Change format of dates from the backslash symbol, reverse the order, and join with hyphens.
                    this.inputQuest.startDate = this.inputQuest.startDate.split("/").reverse().join("-");
                    this.startTime = startDate.split(" ")[1];
                    // Splits by either the + or the - symbol. Removing the timezone.
                    this.startTime = this.startTime.split("+")[0];
                    this.startTime = this.startTime.split("-")[0];

                    this.inputQuest.endDate = new Date(this.inputQuest.endDate).toLocaleString();
                    let endDate = this.quest.endDate;
                    // The date is the values before the comma
                    this.inputQuest.endDate = this.inputQuest.endDate.split(", ")[0];
                    // Change format of dates from the backslash symbol, reverse the order, and join with hyphens.
                    this.inputQuest.endDate = this.inputQuest.endDate.split("/").reverse().join("-");
                    this.endTime = endDate.split(" ")[1];
                    // Splits by either the + or the - symbol. Removing the timezone.
                    this.endTime = this.endTime.split("+")[0];
                    this.endTime = this.endTime.split("-")[0];
                }
            },


            /**
             * Combines dates and times together from input fields and adds :00 on the end for seconds.
             */
            joinDates() {
                let timeOffset = this.formatOffset();

                if(this.startTime.length === 5) {
                    this.startTime += ":00";
                }

                if(this.endTime.length === 5) {
                    this.endTime += ":00";
                }

                this.inputQuest.startDate = this.inputQuest.startDate + " "
                    + this.startTime + timeOffset;

                this.inputQuest.endDate = this.inputQuest.endDate + " "
                    + this.endTime + timeOffset;

                delete this.inputQuest.startTime;
                delete this.inputQuest.endTime;
            },


            /**
             * Gets the local time offset and pads it to be 4 numbers long.
             */
            formatOffset() {
                let timeOffset = (Math.abs(new Date().getTimezoneOffset() / 60)).toString();

                let fullNumber = timeOffset.padStart(2, '0');
                fullNumber = fullNumber.padEnd(4, '0');

                let sign = (new Date().getTimezoneOffset() >= 0) ? "-" : "+";

                return sign + fullNumber;
            },


            /**
             * Displays the countdown alert on the successful saving of a quest.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a quest.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Used to move a treasure hunt in the table up one place.
             *
             * @param rowIndex      the row index of the treasure hunt in the table.
             */
            moveUp(rowIndex) {
                let upIndex = rowIndex - 1;
                let swapRow = this.inputQuest.treasureHunts[rowIndex];
                this.inputQuest.treasureHunts[rowIndex] = this.inputQuest.treasureHunts[upIndex];
                this.inputQuest.treasureHunts[upIndex] = swapRow;
                this.$refs.questTreasureHunt.refresh();
            },


            /**
             * Used to move a treasure hunt in the table down one place.
             *
             * @param rowIndex      the row index of the treasure hunt in the table.
             */
            moveDown(rowIndex) {
                let downIndex = rowIndex + 1;
                let swapRow = this.inputQuest.treasureHunts[rowIndex];
                this.inputQuest.treasureHunts[rowIndex] = this.inputQuest.treasureHunts[downIndex];
                this.inputQuest.treasureHunts[downIndex] = swapRow;
                this.$refs.questTreasureHunt.refresh();
            },


            /**
             * Displays the add treasure hunt component and the search destinations side bar.
             */
            showTreasureHuntComponent() {
                this.addNewTreasureHunt = !this.addNewTreasureHunt;
                this.$emit('TH-side-bar', true);
                this.$emit('Your-TH-side-bar', false);
                //this.treasureHuntSelected = {};
            },


            /**
             * Displays the add treasure hunt component and the your treasure hunts side bar.
             */
            showYourTreasureHuntsComponent() {
                this.addNewTreasureHunt = !this.addNewTreasureHunt;
                this.$emit('Your-TH-side-bar', true);
                this.$emit('TH-side-bar', false);
                //this.treasureHuntSelected = {};
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
            }
        }
    }
</script>

