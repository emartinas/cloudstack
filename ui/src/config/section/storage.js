export default {
  name: 'storage',
  title: 'Storage',
  icon: 'database',
  children: [
    {
      name: 'volume',
      title: 'Volumes',
      icon: 'hdd',
      permission: [ 'listVolumesMetrics', 'listVolumes' ],
      resourceType: 'Volume',
      columns: ['name', 'state', 'type', 'vmname', 'size', 'physicalsize', 'utilization', 'diskkbsread', 'diskkbswrite', 'diskiopstotal', 'storage', 'account', 'zonename'],
      hidden: ['storage', 'utilization'],
      details: ['name', 'id', 'type', 'deviceid', 'sizegb', 'physicalsize', 'provisioningtype', 'utilization', 'diskkbsread', 'diskkbswrite', 'diskioread', 'diskiowrite', 'diskiopstotal', 'path'],
      actions: [
        {
          api: 'createVolume',
          icon: 'plus',
          label: 'Create Volume',
          type: 'main',
          args: ['name', 'zoneid', 'diskofferingid'],
          listView: true
        }, {
          api: 'uploadVolume',
          icon: 'cloud-upload',
          label: 'Upload Volume From URL',
          type: 'main',
          args: ['url', 'name', 'zoneid', 'format', 'diskofferingid', 'checksum'],
          listView: true
        }, {
          api: 'getUploadParamsForVolume',
          icon: 'upload',
          label: 'Upload Local Volume',
          args: ['@file', 'name', 'zoneid', 'format', 'checksum'],
          listView: true
        },
        {
          api: 'attachVolume',
          icon: 'paper-clip',
          label: 'Attach Volume',
          args: ['id', 'virtualmachineid'],
          dataView: true,
          hidden: (record) => { return record.virtualmachineid }
        },
        {
          api: 'detachVolume',
          icon: 'link',
          label: 'Detach Volume',
          args: ['id', 'virtualmachineid'],
          dataView: true,
          hidden: (record) => { return !record.virtualmachineid }
        },
        {
          api: 'migrateVolume',
          icon: 'drag',
          label: 'Migrate Volume',
          args: ['volumeid', 'storageid', 'livemigrate'],
          dataView: true
        },
        {
          api: 'resizeVolume',
          icon: 'fullscreen',
          label: 'Resize Volume',
          type: 'main',
          args: ['id', 'virtualmachineid'],
          dataView: true
        },
        {
          api: 'extractVolume',
          icon: 'cloud-download',
          label: 'Download Volume',
          args: ['id', 'zoneid', 'mode'],
          paramOptions: {
            'mode': {
              'value': 'HTTP_DOWNLOAD'
            }
          },
          dataView: true
        },
        {
          api: 'deleteVolume',
          icon: 'delete',
          label: 'Delete Volume',
          args: ['id'],
          dataView: true,
          groupAction: true
        }
      ]
    },
    {
      name: 'snapshot',
      title: 'Snapshots',
      icon: 'build',
      permission: [ 'listSnapshots' ],
      resourceType: 'Snapshot',
      columns: ['name', 'state', 'volumename', 'intervaltype', 'created', 'account'],
      details: ['name', 'id', 'volumename', 'intervaltype', 'account', 'domain', 'created'],
      actions: [
        {
          api: 'createVolume',
          icon: 'plus',
          label: 'Create volume',
          dataView: true,
          args: [
            'name', 'snapshotid', 'diskofferingid', 'size'
          ]
        },
        {
          api: 'revertSnapshot',
          icon: 'revert',
          label: 'Revert snapshot',
          dataView: true,
          args: [
            'id'
          ]
        },
        {
          api: 'deleteSnapshot',
          icon: 'delete',
          label: 'Delete snapshot',
          dataView: true,
          args: [
            'id'
          ]
        }
      ]
    },
    {
      name: 'vmsnapshot',
      title: 'VM Snapshots',
      icon: 'camera',
      permission: [ 'listVMSnapshot' ],
      resourceType: 'VMSnapshot',
      columns: ['name', 'state', 'type', 'current', 'parent', 'created', 'account'],
      details: ['name', 'id', 'displayname', 'description', 'type', 'current', 'parent', 'virtualmachineid', 'account', 'domain', 'created'],
      actions: [
        {
          api: 'revertToVMSnapshot',
          icon: 'revert',
          label: 'Revert VM snapshot',
          dataView: true,
          args: [
            'vmsnapshotid'
          ]
        },
        {
          api: 'deleteVMSnapshot',
          icon: 'delete',
          label: 'Delete VM Snapshot',
          dataView: true,
          args: [
            'vmsnapshotid'
          ]
        }
      ]
    }
  ]
}