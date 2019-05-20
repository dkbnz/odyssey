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
        methods: {
            formatNames(files) {
                if (files.length === 1) {
                    return files[0].name
                } else {
                    return `${files.length} images selected`
                }
            },
            convertFiles(files) {
                //conversion from array of file objects to desired form for backend sending?
            },
            save() {
                //will close modal and send files away to backend?
                let self = this;
                console.log(self.files[0]);
                fetch(`/v1/savePhotos`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({'photos': self.files})

                }).then(function (response) {
                    self.files = null;
                })
            }
        }
    }
</script>

<style scoped>

</style>