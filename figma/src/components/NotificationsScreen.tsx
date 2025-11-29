import { ArrowLeft } from 'lucide-react';

interface NotificationsScreenProps {
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

export function NotificationsScreen({ onBack }: NotificationsScreenProps) {
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
          <span>Notifications</span>
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto">
        {/* Push Notifications */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PUSH NOTIFICATIONS</span>
          </div>
          <SettingToggle
            label="Push Notifications"
            subtitle="Enable push notifications"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* Matches */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">MATCHES</span>
          </div>
          <SettingToggle
            label="New Matches"
            subtitle="Get notified when you get a new match"
            enabled={true}
            onChange={() => {}}
          />
          <SettingToggle
            label="Match Suggestions"
            subtitle="Receive personalized match suggestions"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* Messages */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">MESSAGES</span>
          </div>
          <SettingToggle
            label="New Messages"
            subtitle="Get notified when you receive a message"
            enabled={true}
            onChange={() => {}}
          />
          <SettingToggle
            label="Message Reminders"
            subtitle="Remind me to respond to messages"
            enabled={false}
            onChange={() => {}}
          />
        </div>

        {/* Activity */}
        <div className="bg-white mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">ACTIVITY</span>
          </div>
          <SettingToggle
            label="Profile Views"
            subtitle="Get notified when someone views your profile"
            enabled={true}
            onChange={() => {}}
          />
          <SettingToggle
            label="Likes"
            subtitle="Get notified when someone likes you"
            enabled={true}
            onChange={() => {}}
          />
        </div>

        {/* Updates */}
        <div className="bg-white mt-4 mb-6">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">UPDATES</span>
          </div>
          <SettingToggle
            label="App Updates"
            subtitle="Get notified about new features and updates"
            enabled={false}
            onChange={() => {}}
          />
          <SettingToggle
            label="Promotional"
            subtitle="Receive promotional offers and news"
            enabled={false}
            onChange={() => {}}
          />
        </div>
      </div>
    </div>
  );
}
