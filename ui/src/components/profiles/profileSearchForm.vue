<template>
    <div>

        <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>

        <b-form @submit.prevent="searchProfiles">
            <!--Input fields for searching for destinations-->
            <b-form-group
                    id="name-field"
                    label="Name:"
                    label-for="name">
                <b-form-input id="name"></b-form-input>
            </b-form-group>
            <b-form-row>
                <b-col>
                    <b-form-group
                            id="nationalities-field"
                            label="Nationality:"
                            label-for="nationality">
                        <b-form-select id="nationality" trim v-model="searchParameters.nationality">
                            <template slot="first">
                                <option :value="null">-- Any --</option>
                            </template>
                            <option :value="nationality.nationality"
                                    v-for="nationality in nationalityOptions">
                                {{nationality.nationality}}
                            </option>
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            id="gender-field"
                            label="Gender:"
                            label-for="gender">
                        <b-form-select :options="genderOptions" id="gender" trim v-model="searchParameters.gender">
                            <template slot="first">
                                <option :value="null">-- Any --</option>
                            </template>
                        </b-form-select>
                    </b-form-group>
                </b-col>
            </b-form-row>
            <b-form-row>
                <b-col>

                    <b-form-group
                            id="travType-field"
                            label="Traveller Type:"
                            label-for="travType">
                        <b-form-select id="travType" trim v-model="searchParameters.travellerType">
                            <template>
                                <option :value="null" selected="selected">-- Any --</option>
                            </template>
                            <option :value="travType.travellerType"
                                    v-for="travType in travellerTypeOptions">
                                {{travType.travellerType}}
                            </option>
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            label="Rank:">
                        <b-input-group>
                            <b-form-input v-model="searchParameters.rank" type="number"></b-form-input>
                            <b-input-group-append>
                                <b-button variant="outline-info" @click="searchParameters.rank = userProfile.achievementTracker.rank">My Rank</b-button>
                            </b-input-group-append>
                        </b-input-group>
                    </b-form-group>
                </b-col>
            </b-form-row>
            <b-form-row>
                <b-col>
                    <b-form-group
                            id="minAge-field"
                            label="Min Age: "
                            label-for="minAge">
                        <div class="mt-2">{{ searchParameters.minAge }}</div>

                        <!--Range slider from 0 to 110-->
                        <b-form-input :type="'range'" id="minAge"
                                      max="110"
                                      min="0"
                                      trim
                                      v-model="searchParameters.minAge"></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            id="maxAge-field"
                            label="Max Age:"
                            label-for="maxAge">
                        <div class="mt-2">{{ searchParameters.maxAge }}</div>
                        <!--Range slider from 0 to 110-->
                        <b-form-input :type="'range'" id="maxAge"
                                      max="120"
                                      min="0"
                                      trim
                                      v-model="searchParameters.maxAge"></b-form-input>
                    </b-form-group>
                </b-col>
            </b-form-row>
            <b-button @click="searchProfiles" block variant="primary" type="submit">Search</b-button>
        </b-form>
    </div>
</template>

<script>
    export default {
        name: "profileSearchForm",

        props: {
            userProfile: Object
        },

        data() {
            return {
                searchParameters: {
                    nationality: "",
                    gender: "",
                    minAge: 0,
                    maxAge: 120,
                    travellerType: "",
                    rank: null
                },
                travellerTypeOptions: [],
                nationalityOptions: [],
                showError: false,
                alertMessage: "",
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ]
            }
        },

        mounted() {
            this.getTravellerTypes();
            this.getNationalities();
        },

        methods: {
            /**
             * Changes fields so that they can be used in searching.
             * Runs validation on range fields.
             */
            searchProfiles() {
                this.searchParameters.minAge = parseInt(this.searchParameters.minAge);
                this.searchParameters.maxAge = parseInt(this.searchParameters.maxAge);
                if (isNaN(this.searchParameters.minAge) || isNaN(this.searchParameters.maxAge)) {
                    this.showError = true;
                    this.alertMessage = "Min or Max Age are not numbers";
                } else if (this.searchParameters.minAge > this.searchParameters.maxAge) {
                    this.showError = true;
                    this.alertMessage = "Min age is greater than max age";
                } else {
                    this.showError = false;
                    this.$emit('search', this.searchParameters);
                }
            },


            /**
             * Retrieves all the traveller types.
             */
            getTravellerTypes() {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(travellerTypes => this.travellerTypeOptions = travellerTypes);
            },


            /**
             * Retrieves all the nationalities.
             */
            getNationalities() {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(nationalities => this.nationalityOptions = nationalities);
            },
        }

    }
</script>