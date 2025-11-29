import { ArrowLeft, ChevronRight } from 'lucide-react';

interface SettingsScreenProps {
  onBack: () => void;
}

interface SettingToggleProps {
  label: string;
  subtitle?: string;
  enabled: boolean;
  onChange: (enabled: boolean) => void;
}

function SettingToggle({ label, subtitle, enabled, onChange }: SettingToggleProps) {
  return (
    <div className="flex items-center justify-between p-4 hover:bg-gray-50">
      <div className="flex-1">
        <div className="text-gray-900">{label}</div>
        {subtitle && <div className="text-xs text-gray-500 mt-0.5">{subtitle}</div>}
      </div>
      <button
        onClick={() => onChange(!enabled)}
        className={`relative w-12 h-6 rounded-full transition-colors ${
          enabled ? 'bg-pink-500' : 'bg-gray-300'
        }`}
      >
        <div
          className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full transition-transform ${
            enabled ? 'translate-x-6' : 'translate-x-0'
          }`}
        />
      </button>
    </div>
  );
}

interface SettingItemProps {
  label: string;
  value: string;
  onClick: () => void;
}

function SettingItem({ label, value, onClick }: SettingItemProps) {
  return (
    <button
      onClick={onClick}
      className="w-full flex items-center justify-between p-4 hover:bg-gray-50 transition-colors"
    >
      <div className="text-gray-900">{label}</div>
      <div className="flex items-center gap-2">
        <span className="text-sm text-gray-500">{value}</span>
        <ChevronRight className="w-5 h-5 text-gray-400" />
      </div>
    </button>
  );
}

export function SettingsScreen({ onBack }: SettingsScreenProps) {
  return (
    <div className="h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <div className="bg-white px-6 py-4 border-b border-gray-200">
        <div className="flex items-center gap-4">
          <button
            onClick={onBack}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <span>Settings</span>
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto">
        {/* Account Settings */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">ACCOUNT</span>
          </div>
          <SettingItem label="Phone Number" value="+1 234 567 8900" onClick={() => {}} />
          <SettingItem label="Email" value="john@example.com" onClick={() => {}} />
          <SettingItem label="Change Password" value="" onClick={() => {}} />
        </div>

        {/* Discovery Settings */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">DISCOVERY</span>
          </div>
          <SettingItem label="Location" value="New York, NY" onClick={() => {}} />
          <SettingItem label="Maximum Distance" value="50 km" onClick={() => {}} />
          <SettingItem label="Age Range" value="25-35" onClick={() => {}} />
          <SettingToggle
            label="Show me on the app"
            subtitle="Be visible to other users"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* Privacy Settings */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PRIVACY</span>
          </div>
          <SettingToggle
            label="Hide last seen"
            subtitle="Others won't see when you were last active"
            enabled={false}
            onChange={() => {}}
          />
          <SettingToggle
            label="Hide online status"
            subtitle="Others won't see when you're online"
            enabled={false}
            onChange={() => {}}
          />
          <SettingToggle
            label="Read receipts"
            subtitle="Let others know you've read their messages"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* App Settings */}
        <div className="bg-white mt-4 mb-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">APP</span>
          </div>
          <SettingItem label="Language" value="English" onClick={() => {}} />
          <SettingItem label="Theme" value="Light" onClick={() => {}} />
          <SettingToggle
            label="Haptic Feedback"
            subtitle="Vibrate on interactions"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* Danger Zone */}
        <div className="bg-white mt-4 mb-6">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">DANGER ZONE</span>
          </div>
          <button className="w-full p-4 text-left text-red-500 hover:bg-red-50 transition-colors">
            Delete Account
          </button>
        </div>
      </div>
    </div>
  );
}
