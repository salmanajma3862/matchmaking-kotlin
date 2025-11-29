import { ImageWithFallback } from './figma/ImageWithFallback';
import {
  User,
  Settings,
  Bell,
  Shield,
  Heart,
  CreditCard,
  HelpCircle,
  FileText,
  LogOut,
  ChevronRight,
  Camera,
  Edit3,
} from 'lucide-react';

interface UserData {
  name: string;
  age: number;
  image: string;
  city?: string;
  profileCompletion: number;
  matchCount: number;
  viewCount: number;
}

interface ProfileScreenProps {
  user: UserData;
  onViewProfile: () => void;
  onEditProfile: () => void;
  onSettings: () => void;
  onNotifications: () => void;
  onPrivacy: () => void;
  onSubscription: () => void;
  onHelp: () => void;
  onAbout: () => void;
  onLogout: () => void;
}

interface MenuItemProps {
  icon: React.ReactNode;
  label: string;
  subtitle?: string;
  onClick: () => void;
  iconBgColor?: string;
  showBadge?: boolean;
  badgeCount?: number;
}

function MenuItem({ icon, label, subtitle, onClick, iconBgColor = 'bg-gray-100', showBadge, badgeCount }: MenuItemProps) {
  return (
    <button
      onClick={onClick}
      className="w-full flex items-center gap-4 p-4 hover:bg-gray-50 transition-colors active:bg-gray-100"
    >
      <div className={`${iconBgColor} p-3 rounded-full flex items-center justify-center relative`}>
        {icon}
        {showBadge && badgeCount && badgeCount > 0 && (
          <div className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
            {badgeCount}
          </div>
        )}
      </div>
      <div className="flex-1 text-left">
        <div className="text-gray-900">{label}</div>
        {subtitle && <div className="text-xs text-gray-500 mt-0.5">{subtitle}</div>}
      </div>
      <ChevronRight className="w-5 h-5 text-gray-400" />
    </button>
  );
}

export function ProfileScreen({
  user,
  onViewProfile,
  onEditProfile,
  onSettings,
  onNotifications,
  onPrivacy,
  onSubscription,
  onHelp,
  onAbout,
  onLogout,
}: ProfileScreenProps) {
  return (
    <div className="h-screen bg-gray-50 flex flex-col overflow-y-auto">
      {/* Header */}
      <div className="bg-gradient-to-br from-pink-500 to-rose-500 px-6 pt-12 pb-8">
        <div className="flex flex-col items-center">
          {/* Profile Picture */}
          <div className="relative mb-4">
            <div className="w-28 h-28 rounded-full border-4 border-white shadow-lg overflow-hidden">
              <ImageWithFallback
                src={user.image}
                alt={user.name}
                className="w-full h-full object-cover"
              />
            </div>
            <button
              onClick={onEditProfile}
              className="absolute bottom-0 right-0 bg-white p-2 rounded-full shadow-lg hover:shadow-xl transition-all"
            >
              <Camera className="w-4 h-4 text-pink-500" />
            </button>
          </div>

          {/* Name & Location */}
          <h1 className="text-white text-2xl mb-1">{user.name}, {user.age}</h1>
          {user.city && (
            <p className="text-white/90 text-sm mb-4">{user.city}</p>
          )}

          {/* Stats */}
          <div className="flex gap-8 mt-2">
            <div className="text-center">
              <div className="text-white text-xl">{user.matchCount}</div>
              <div className="text-white/80 text-xs">Matches</div>
            </div>
            <div className="text-center">
              <div className="text-white text-xl">{user.viewCount}</div>
              <div className="text-white/80 text-xs">Profile Views</div>
            </div>
            <div className="text-center">
              <div className="text-white text-xl">{user.profileCompletion}%</div>
              <div className="text-white/80 text-xs">Complete</div>
            </div>
          </div>
        </div>
      </div>

      {/* Profile Completion */}
      {user.profileCompletion < 100 && (
        <div className="bg-gradient-to-r from-pink-50 to-rose-50 mx-4 -mt-6 rounded-2xl shadow-md p-4 mb-4 border border-pink-100">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-pink-700">Complete your profile</span>
            <span className="text-sm text-pink-600">{user.profileCompletion}%</span>
          </div>
          <div className="w-full bg-white rounded-full h-2 overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-pink-500 to-rose-500 transition-all duration-300"
              style={{ width: `${user.profileCompletion}%` }}
            />
          </div>
          <p className="text-xs text-pink-600 mt-2">A complete profile gets more matches!</p>
        </div>
      )}

      {/* Menu Sections */}
      <div className="flex-1 pb-6">
        {/* Account Section */}
        <div className="bg-white mb-4 mt-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">ACCOUNT</span>
          </div>
          <MenuItem
            icon={<User className="w-5 h-5 text-pink-600" />}
            label="View My Public Profile"
            subtitle="See how others view your profile"
            onClick={onViewProfile}
            iconBgColor="bg-pink-50"
          />
          <MenuItem
            icon={<Edit3 className="w-5 h-5 text-purple-600" />}
            label="Edit Profile"
            subtitle="Update your information and photos"
            onClick={onEditProfile}
            iconBgColor="bg-purple-50"
          />
          <MenuItem
            icon={<Settings className="w-5 h-5 text-gray-700" />}
            label="Settings"
            subtitle="App preferences and privacy"
            onClick={onSettings}
            iconBgColor="bg-gray-100"
          />
          <MenuItem
            icon={<Bell className="w-5 h-5 text-orange-600" />}
            label="Notifications"
            subtitle="Manage notification preferences"
            onClick={onNotifications}
            iconBgColor="bg-orange-50"
            showBadge
            badgeCount={3}
          />
          <MenuItem
            icon={<Shield className="w-5 h-5 text-blue-600" />}
            label="Privacy & Safety"
            subtitle="Control your visibility and safety"
            onClick={onPrivacy}
            iconBgColor="bg-blue-50"
          />
        </div>

        {/* Premium Section */}
        <div className="bg-white mb-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">PREMIUM</span>
          </div>
          <MenuItem
            icon={<Heart className="w-5 h-5 text-pink-600" />}
            label="Upgrade to Premium"
            subtitle="Get unlimited likes and more features"
            onClick={onSubscription}
            iconBgColor="bg-pink-50"
          />
          <MenuItem
            icon={<CreditCard className="w-5 h-5 text-purple-600" />}
            label="Payment & Billing"
            subtitle="Manage your subscription"
            onClick={onSubscription}
            iconBgColor="bg-purple-50"
          />
        </div>

        {/* Support Section */}
        <div className="bg-white mb-4">
          <div className="px-6 py-3 border-b border-gray-100">
            <span className="text-xs text-gray-500">SUPPORT</span>
          </div>
          <MenuItem
            icon={<HelpCircle className="w-5 h-5 text-green-600" />}
            label="Help & Support"
            subtitle="Get help and contact us"
            onClick={onHelp}
            iconBgColor="bg-green-50"
          />
          <MenuItem
            icon={<FileText className="w-5 h-5 text-indigo-600" />}
            label="Terms & About"
            subtitle="Legal information and app details"
            onClick={onAbout}
            iconBgColor="bg-indigo-50"
          />
        </div>

        {/* Logout */}
        <div className="bg-white">
          <button
            onClick={onLogout}
            className="w-full flex items-center justify-center gap-2 py-4 text-red-500 hover:bg-red-50 transition-colors"
          >
            <LogOut className="w-5 h-5" />
            <span>Logout</span>
          </button>
        </div>

        {/* App Version */}
        <div className="text-center mt-6 px-6">
          <p className="text-xs text-gray-400">Version 1.0.0</p>
          <p className="text-xs text-gray-400 mt-1">Made with ❤️</p>
        </div>
      </div>
    </div>
  );
}
