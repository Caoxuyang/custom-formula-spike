This is a spike repo to spike the best option for a customer to leveage custom formula to achieve 1 ~ N.
This repo will test three different potential options to integrate MSFT formula & custom formula with a specifically designed test scenario.

## Spike Plan

Apps:
- A: Init s3 client & upload single object & delete single object
- B: Init s3 client & upload single object + set metadata before uploading object
- C: Init s3 client & upload single object + set metadata before uploading object

options:
1. Include both `code diff` generated from `MSFT formula` and `custom code change` into a big custom formula. So option 1's `custom formula` = `MSFT formula generated diff` + `custom code diff`
1. Another way to compare with option 1, generate a custom formula base on the diff from `custom code change`, and manually copy the custom formula generate into MSFT formula. So option 2's `custom formula` = `MSFT formula` + `custom code diff`
1. Custom formula only contains the manual code changes from the user, it won't include any MSFT formula or diff generated from MSFt formula. Everytime customer want to apply their custom formula to a brand new repo, they should first apply MSFT formula, then apply the custom formula. So option 3's `custom formula` = `custom code diff`

Scenario:
1. First apply MSFT formula on app A, this generate a code commit `A-msft`
1. Manually introduce code change based on step 1, `change the storage blob authn from use endpoint + DefaultAzureCredentials -> connectionString`, this generate a code commit `A-custom-use-connectionstring`
1. Construct our first custom formula `A-formula-option-{1/2/3}` based on three options above.
1. Migrate app B, apply the custom formula `A-formula-option-{1/2/3}` to app B.
1. Manually intrudce code change based on step 4, assuming the customer has a policy that every object uploaded should be attched with a metadata to indicate the current utc timestamp `Add metadata before uploading the object`. This generate a code commit `B-custom-attach-metadata`
1. Construct the custom formula again to include code commit `B-custom-attach-metadata` and generate custom formula `A+B-formula-option-{1/2/3}`
1. Apply the `A+B-formula-option-{1/2/3}` to C to check the finial results.


As you can see, two challenges introduced:
-  `change the storage blob authn from use endpoint + DefaultAzureCredentials -> connectionString` this is a generate code path that three apps will all go through, to init the blob client. This is to test a comflict / priority race condition that the MSFT formula tells LLM to use `endpoint + DefaultAzureCredentials` while custom formula tells LLM to use `connectionString`. 
- `Add metadata before uploading the object` is another case to test whether custom formula can handle these customer specific operations well.