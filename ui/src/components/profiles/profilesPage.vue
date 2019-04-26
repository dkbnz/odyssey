<template>
    <div class="container">
        <h1 class="page_title">Find Profiles</h1>
        <p class="page_title"><i>Search for other travellers using the form below</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>There's something wrong in the form!</b-alert>
        <div>
            <b-form-group
                    id="nationalities-field"
                    label="Nationalities:"
                    label-for="nationality">
                <b-form-select id="nationality" v-model="nationalities" trim>
                    <template slot="first">
                        <option :value="null" >-- Any --</option>
                    </template>
                    <option v-for="nationality in nationalityOptions">{{nationality.nationality}}</option>
                </b-form-select>
            </b-form-group>
            <b-form-group
                    id="gender-field"
                    label="Gender:"
                    label-for="gender">
                <b-form-select id="gender" v-model="gender" :options="genderOptions" trim>
                    <template slot="first">
                        <option :value="null" >-- Any --</option>
                    </template>
                </b-form-select>
            </b-form-group>
            <b-form-group
                    id="minAge-field"
                    label="Min Age:"
                    label-for="minAge">
                <b-form-input id="minAge" v-model="minAge" :type="'range'" min="0" max="150" trim></b-form-input>
                <div class="mt-2">Value: {{ minAge }}</div>
            </b-form-group>
            <b-form-group
                    id="maxAge-field"
                    label="Max Age:"
                    label-for="maxAge">
                <b-form-input id="maxAge" v-model="maxAge" :type="'range'" min="0" max="150" trim></b-form-input>
                <div class="mt-2">Value: {{ maxAge }}</div>
            </b-form-group>
            <b-form-group
                    id="travType-field"
                    label="Traveller Type(s):"
                    label-for="travType">
                <b-form-select id="travType" v-model="travType" trim>
                    <template slot="first">
                        <option :value="null" >-- Any --</option>
                    </template>
                    <option v-for="travType in travTypeOptions">{{travType.travellerType}}</option>
                </b-form-select>
            </b-form-group>
            <b-button block @click="searchProfiles">Search</b-button>
        </div>
        <div style="margin-top: 40px">
            <b-table hover striped outlined
                     id="myFutureTrips"
                     :items="profiles"
                     :fields="fields"
                     :per-page="perPage"
                     :current-page="currentPage"
            >
                <template slot="actions" slot-scope="row">
                    <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} More Details
                    </b-button>
                </template>
                <template slot="row-details" slot-scope="row">
                    <b-card>
                        <view-profile></view-profile>
                    </b-card>
                </template>
            </b-table>
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="profiles-field"
                            label-for="perPage">
                        <b-form-select id="perPage" v-model="perPage" :options="optionViews" size="sm" trim> </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPage"
                            :total-rows="rows"
                            :per-page="perPage"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            align="center"
                            size="sm"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>
    </div>

</template>

<script>
    import viewProfile from '../dash/viewProfile.vue'
    export default {
        name: "profilesPage",
        created() {
            document.title = "TravelEA - Profiles";
        },

        data: function() {
            return {
                showError: false,
                nationalities: null,
                gender: null,
                minAge: 0,
                maxAge: 150,
                travType: null,
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 5,
                currentPage: 1,
                nationalityOptions: [],
                genderOptions: [
                    {value: 'male', text: 'Male'},
                    {value: 'female', text: 'Female'},
                    {value: 'other', text: 'Other'}
                ],
                travTypeOptions: [],
                fields: ['first_name', 'last_name', 'nationality', 'gender', 'age', 'traveller_type', 'actions'],
                profiles: [
                    {first_name:'Isaac'},
                    {first_name:'Jack'},
                    {first_name:'Jill'},
                    {first_name:'John'},
                    {first_name:'James'},
                    {first_name:'Jerry'}]
            }
        },
        mounted () {
            this.getNationalities(nationalities => this.nationalityOptions = nationalities);
            this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
        },
        methods: {
            getNationalities (cb) {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            getTravTypes (cb) {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },

            checkStatus (response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error); // eslint-disable-line no-console
                throw error;
            },
            parseJSON (response) {
                return response.json();
            },
            searchProfiles() {
                this.minAge = parseInt(this.minAge);
                this.maxAge =  parseInt(this.maxAge);
                if (isNaN(this.minAge) || isNaN(this.maxAge)) {
                    this.showError = true;
                } else if (this.minAge > this.maxAge) {
                    this.showError = true;
                }
                else {
                    this.showError = false;
                    console.log(this.nationalities, this.gender, this.minAge, this.maxAge, this.travType)
                }
            }
        },
        computed: {
            rows() {
                return this.profiles.length
            }
        },
        components: {
            viewProfile
        }
    }
</script>

<style scoped>

</style>