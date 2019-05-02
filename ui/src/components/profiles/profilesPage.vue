<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <div class="container">
            <h1 class="page_title">Find Profiles</h1>
            <p class="page_title"><i>Search for other travellers using the form below</i></p>
            <b-alert v-model="showError" variant="danger" dismissible>There's something wrong in the form!</b-alert>
            <div>
                <b-form-group
                        id="nationalities-field"
                        label="Nationality:"
                        label-for="nationality">
                    <b-form-select id="nationality" v-model="searchNationality" trim>
                        <template slot="first">
                            <option :value="null" >-- Any --</option>
                        </template>
                        <option v-for="nationality in nationalityOptions" :value="nationality.nationality">{{nationality.nationality}}</option>
                    </b-form-select>
                </b-form-group>
                <b-form-group
                        id="gender-field"
                        label="Gender:"
                        label-for="gender">
                    <b-form-select id="gender" v-model="searchGender" :options="genderOptions" trim>
                        <template slot="first">
                            <option :value="null" >-- Any --</option>
                        </template>
                    </b-form-select>
                </b-form-group>
                <b-form-group
                        id="minAge-field"
                        label="Min Age: "
                        label-for="minAge">
                    <div class="mt-2">{{ searchMinAge }}</div>
                    <b-form-input id="minAge" v-model="searchMinAge" :type="'range'" min="0" max="150" trim></b-form-input>

                </b-form-group>
                <b-form-group
                        id="maxAge-field"
                        label="Max Age:"
                        label-for="maxAge">
                    <div class="mt-2">{{ searchMaxAge }}</div>
                    <b-form-input id="maxAge" v-model="searchMaxAge" :type="'range'" min="0" max="150" trim></b-form-input>
                </b-form-group>
                <b-form-group
                        id="travType-field"
                        label="Traveller Type:"
                        label-for="travType">
                    <b-form-select id="travType" v-model="searchTravType" trim>
                        <template>
                            <option :value="null" selected="selected">-- Any --</option>
                        </template>
                        <option v-for="travType in travTypeOptions" :value="travType.travellerType">{{travType.travellerType}}</option>
                    </b-form-select>
                </b-form-group>
                <b-button block variant="primary" @click="searchProfiles">Search</b-button>
            </div>
            <div style="margin-top: 40px">
                <b-table hover striped outlined
                         id="profiles"
                         ref="profilesTable"
                         :items="profiles"
                         :fields="fields"
                         :per-page="perPage"
                         :current-page="currentPage"
                         :sort-by.sync="sortBy"
                         :sort-desc.sync="sortDesc"
                         :busy="profiles.length === 0"
                >
                    <div slot="table-busy" class="text-center text-danger my-2">
                        <b-spinner class="align-middle"></b-spinner>
                        <strong>Loading...</strong>
                    </div>
                    <template slot="actions" slot-scope="row">
                        <b-button v-if="profile.is_admin && !(row.item.is_admin)" size="sm" @click="makeAdmin(row.item)" variant="success" class="mr-2">
                            Make Admin
                        </b-button>
                        <b-button v-if="profile.is_admin && row.item.is_admin" :disabled="row.item.username==='admin@travelea.com'" variant="danger" size="sm" @click="removeAdmin(row.item)" class="mr-2">
                            Remove Admin
                        </b-button>
                        <b-button size="sm" @click="row.toggleDetails" variant="warning" class="mr-2">
                            {{ row.detailsShowing ? 'Hide' : 'Show'}} More Details
                        </b-button>
                        <b-button v-if="profile.is_admin" :disabled="row.item.username==='admin@travelea.com'" size="sm" variant="danger" @click="makeAdmin(row.item)" class="mr-2">
                            Delete
                        </b-button>
                    </template>
                    <template slot="row-details" slot-scope="row">
                        <b-card>
                            <view-profile :profile="row.item"></view-profile>
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
        <footer-main></footer-main>
    </div>

</template>

<script>
    import viewProfile from '../dash/viewProfile.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    export default {
        name: "profilesPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        created() {
            document.title = "TravelEA - Profiles";
        },

        data: function() {
            return {
                sortBy: 'firstName',
                sortDesc: false,
                showError: false,
                searchNationality: "",
                searchGender: "",
                searchMinAge: 0,
                searchMaxAge: 100,
                searchTravType: "",
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 10,
                currentPage: 1,
                genderOptions: [
                    {value: 'male', text: 'Male'},
                    {value: 'female', text: 'Female'},
                    {value: 'other', text: 'Other'}
                ],
                fields: [{key:'firstName', label: "First Name", sortable: true}, {key:'lastName', label: "Last Name", sortable: true}, {key:'nationalities[0].nationality', label: "Nationality", sortable: true}, {key:'gender', value: 'gender', sortable: true}, {key:'age', value:'age', sortable: true}, {key:'travellerTypes[0].travellerType', label: "Traveller Types" , sortable: true}, 'actions'],
                profiles: []
            }
        },
        mounted () {
            this.queryProfiles();
            //this.getProfiles(profiles => this.profiles = profiles);
        },
        methods: {
            parseJSON (response) {
                return response.json();
            },
            makeAdmin(profile) {
                let self = this;
                fetch('/v1/makeAdmin/' + profile.id, {
                    method: 'POST',
                }).then(function() {
                    self.searchProfiles();
                })
            },
            removeAdmin(profile) {
                let self = this;
                fetch('/v1/removeAdmin/' + profile.id, {
                    method: 'POST',
                }).then(function() {
                    self.searchProfiles();
                })
            },
            searchProfiles() {
                this.searchMinAge = parseInt(this.searchMinAge);
                this.searchMaxAge =  parseInt(this.searchMaxAge);
                if (isNaN(this.searchMinAge) || isNaN(this.searchMaxAge)) {
                    this.showError = true;
                } else if (this.searchMinAge > this.searchMaxAge) {
                    this.showError = true;
                }
                else {
                    if (this.searchTravType === null) {
                        this.searchTravType = "";
                    }
                    if (this.searchNationality === null) {
                        this.searchNationality = "";
                    }
                    if (this.searchGender === null) {
                        this.searchGender = "";
                    }
                    this.showError = false;
                    this.queryProfiles();

                }
            },
            queryProfiles () {
                let searchQuery = "?nationality=" + this.searchNationality + "&gender=" + this.searchGender + "&min_age=" + this.searchMinAge + "&max_age=" + this.searchMaxAge + "&traveller_type=" + this.searchTravType;
                return fetch(`/v1/profiles` + searchQuery,  {

                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.profiles = data;
                    })
            },
        },
        computed: {
            rows() {
                return this.profiles.length
            }
        },
        components: {
            viewProfile,
            NavBarMain,
            FooterMain
        }
    }
</script>

<style scoped>

</style>