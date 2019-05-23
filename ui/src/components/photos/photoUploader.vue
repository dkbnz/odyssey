<template>
    <div>
        <b-form-file :file-name-formatter="formatNames" :state="Boolean(files)" accept="image/*"
                     drop-placeholder="Drop files here..."
                     multiple
                     placeholder="Choose files..."
                     v-model="files"
        ></b-form-file>
        <b-button @click="files = null" class="btn btn-info">Clear Selected Files</b-button>
        <b-button @click="save()" class="btn btn-info">Save</b-button>
    </div>
</template>

<script>
    export default {
        name: "photoUploader",
        data: function () {
            return {
                files: null
            }
        },
        props: ['files'],
        methods: {
            formatNames(files) {
                if (files.length === 1) {
                    return files[0].name
                } else {
                    return `${files.length} images selected`
                }
            },
            save(){
                this.$emit('save-photos', this.files);
                this.files = null;
                }
            }

    }
</script>

<style scoped>

</style>